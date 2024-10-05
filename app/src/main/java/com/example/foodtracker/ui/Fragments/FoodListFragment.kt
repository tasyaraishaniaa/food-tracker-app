package com.example.foodtracker.ui.Fragments

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

class FoodListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var database: AppDatabase
    private lateinit var postList: List<Post>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)

        database = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "app_database"
        ).build()

        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        postList = listOf()

        postAdapter = PostAdapter(requireContext(), postList)
        recyclerView.adapter = postAdapter

        loadPosts()

        return view
    }


    private fun loadPosts() {
        CoroutineScope(Dispatchers.IO).launch {
            postList = database.postDao().getAllPosts()
            withContext(Dispatchers.Main) {
                postAdapter = PostAdapter(requireContext(), postList)
                recyclerView.adapter = postAdapter
            }
        }
    }
}