package com.example.movieapp.data.remote.local.entity


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.data.remote.dao.BookmarkDao

@Database(
    entities = [BookmarkedMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        const val DATABASE_NAME = "movie_database"
    }
}