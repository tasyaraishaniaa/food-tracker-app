package com.example.foodtracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val photoUri: String,
    val foodName: String,
    val description: String,
    val date: String
)