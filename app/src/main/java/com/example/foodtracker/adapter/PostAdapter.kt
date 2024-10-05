package com.example.foodtracker.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodtracker.DetailFoodActivity
import com.example.foodtracker.R
import com.example.foodtracker.data.model.Post

class PostAdapter(
    private val context: Context,
    private val postList: List<Post>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodNameTextView: TextView = itemView.findViewById(R.id.foodNameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val postDateTextView: TextView = itemView.findViewById(R.id.postDateTextView)
        val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.foodNameTextView.text = post.foodName
        holder.descriptionTextView.text = post.description
        holder.postDateTextView.text = post.date
        holder.postImageView.setImageURI(Uri.parse(post.photoUri))

        // Menambahkan klik listener pada item
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailFoodActivity::class.java).apply {
                putExtra("foodName", post.foodName)
                putExtra("description", post.description)
                putExtra("photoUri", post.photoUri)
                putExtra("date", post.date)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = postList.size
}