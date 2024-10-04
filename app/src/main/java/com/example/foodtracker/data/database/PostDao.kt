package com.example.foodtracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodtracker.data.model.Post

@Dao
interface PostDao {
    @Insert
    fun insertPost(post: Post)

    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<Post>
}