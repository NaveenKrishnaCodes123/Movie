package com.example.movieapp.data.remote.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_movies")
data class BookmarkedMovieEntity(
    @PrimaryKey
    val movieId: String,
    val timestamp: Long = System.currentTimeMillis()
)