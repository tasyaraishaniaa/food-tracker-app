package com.example.foodtracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodtracker.data.model.Post

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}