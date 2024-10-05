package com.example.foodtracker

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailFoodActivity : AppCompatActivity() {

    private lateinit var foodNameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var postDateTextView: TextView
    private lateinit var postImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_food)

        // Inisialisasi view menggunakan findViewById setelah setContentView dipanggil
        foodNameTextView = findViewById(R.id.foodNameTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        postDateTextView = findViewById(R.id.postDateTextView)
        postImageView = findViewById(R.id.postImageView)

        // Mendapatkan data dari intent
        val foodName = intent.getStringExtra("foodName")
        val description = intent.getStringExtra("description")
        val photoUri = intent.getStringExtra("photoUri")
        val date = intent.getStringExtra("date")

        // Memeriksa apakah view telah di-inisialisasi dengan benar
        if (foodName != null) {
            foodNameTextView.text = foodName
        }
        if (description != null) {
            descriptionTextView.text = description
        }
        if (date != null) {
            postDateTextView.text = date
        }
        if (photoUri != null) {
            postImageView.setImageURI(Uri.parse(photoUri))
        }
    }
}