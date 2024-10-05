package com.example.foodtracker.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodtracker.R
import com.example.foodtracker.adapter.PostAdapter
import com.example.foodtracker.data.database.AppDatabase
import com.example.foodtracker.data.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var database: AppDatabase
    private lateinit var postList: List<Post>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //inisialisasi database
        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "app_database"
        ).build()

        //inisialisasi recyclerview
        recyclerView = view.findViewById(R.id.recyclerView)

        // Set LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true) // Mengoptimalkan ukuran jika item RecyclerView memiliki ukuran tetap

        //postList sementara supaya tidak error
        postList = listOf()

        //inisialisasi adapter
        postAdapter = PostAdapter(requireContext(), postList)
        recyclerView.adapter = postAdapter

        //Load data
        loadPosts()

        return view
    }


    private fun loadPosts() {
        CoroutineScope(Dispatchers.IO).launch {
            postList = database.postDao().getAllPosts() // Mendapatkan data dari database
            withContext(Dispatchers.Main) {
                postAdapter = PostAdapter(requireContext(), postList)
                recyclerView.adapter = postAdapter
            }
        }
    }
}