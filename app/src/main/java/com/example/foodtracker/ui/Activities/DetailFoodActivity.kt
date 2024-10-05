package com.example.foodtracker.ui.Activities

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodtracker.R

class DetailFoodActivity : AppCompatActivity() {

    private lateinit var foodNameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var postDateTextView: TextView
    private lateinit var postImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_food)

        foodNameTextView = findViewById(R.id.foodNameTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        postDateTextView = findViewById(R.id.postDateTextView)
        postImageView = findViewById(R.id.postImageView)

        val foodName = intent.getStringExtra("foodName")
        val description = intent.getStringExtra("description")
        val photoUri = intent.getStringExtra("photoUri")
        val date = intent.getStringExtra("date")

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