package com.example.foodtracker.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.foodtracker.R
import com.example.foodtracker.data.database.AppDatabase
import com.example.foodtracker.data.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {

    private val REQUEST_CODE_PERMISSIONS = 100
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_PHOTO = 2

    private lateinit var nameTextView: TextView
    private lateinit var cameraButton: Button
    private lateinit var galleryButton: Button
    private lateinit var selectedImageUri: Uri
    private lateinit var selectedImageView: ImageView
    private lateinit var foodNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var postButton: Button

    private lateinit var database: AppDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "app_database"
        ).build()

        checkAndRequestPermissions()

        nameTextView = view.findViewById(R.id.nameTextView)
        cameraButton = view.findViewById(R.id.cameraButton)
        galleryButton = view.findViewById(R.id.galleryButton)
        selectedImageView = view.findViewById(R.id.selectedImageView)
        foodNameEditText = view.findViewById(R.id.foodNameEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        dateTextView = view.findViewById(R.id.dateTextView)
        postButton = view.findViewById(R.id.postButton)

        val name = getName()
        val dateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDate = dateTime.format(formatter)

        nameTextView.text = "Name: $name"
        dateTextView.text = "Date: $formattedDate"

        cameraButton.setOnClickListener { openCamera() }
        galleryButton.setOnClickListener { openGallery() }

        postButton.setOnClickListener {
            val foodName = foodNameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            if (name != null && selectedImageUri != Uri.EMPTY && foodName.isNotBlank()) {
                savePost(name, selectedImageUri.toString(), foodName, description, formattedDate)
                findNavController().navigate(R.id.action_dashboardFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Please complete all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun checkAndRequestPermissions() {
        if (!allPermissionsGranted()) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(requireContext(), "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_PICK_PHOTO)
    }

    private fun saveImageToStorage(bitmap: Bitmap): Uri? {
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captured_image_${System.currentTimeMillis()}.jpg")

        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val photoUri = saveImageToStorage(it)
                        selectedImageUri = photoUri ?: Uri.EMPTY
                        selectedImageView.setImageBitmap(it)
                    }
                }
                REQUEST_PICK_PHOTO -> {
                    selectedImageUri = data?.data ?: Uri.EMPTY
                    if (selectedImageUri != Uri.EMPTY) {
                        selectedImageView.setImageURI(selectedImageUri)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePost(name: String, photoUri: String, foodName: String, description: String, date: String) {
        val post = Post(
            id = 0,
            name = name,
            photoUri = photoUri,
            foodName = foodName,
            description = description,
            date = date
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.postDao().insertPost(post)
            } catch (e: Exception) {
                e.printStackTrace() // Log the error
            }
        }
    }

    private fun getName(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("name", null)
    }
}