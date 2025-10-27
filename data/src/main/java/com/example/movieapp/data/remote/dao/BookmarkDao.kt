package com.example.movieapp.data.remote.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.remote.local.entity.BookmarkedMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkMovie(movie: BookmarkedMovieEntity): Unit


    @Query("DELETE FROM bookmarked_movies WHERE movieId = :movieId")
    suspend fun removeBookmark(movieId: String): Int

    @Query("SELECT * FROM bookmarked_movies")
    fun getBookmarkedMovies(): Flow<List<BookmarkedMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_movies WHERE movieId = :movieId)")
    suspend fun isMovieBookmarked(movieId: String): Boolean
}