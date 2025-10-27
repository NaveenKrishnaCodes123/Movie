package com.example.movieapp.domain.model.repository


import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Result<List<Movie>>
    suspend fun getMovieById(id: String): Result<Movie>
    suspend fun searchMovies(query: String): Result<List<Movie>>
    suspend fun sortMovies(sortType: SortType): Result<List<Movie>>
    suspend fun toggleBookmark(movieId: String, isBookmarked: Boolean)
    fun getBookmarkedMovies(): Flow<List<Movie>>
    suspend fun isMovieBookmarked(movieId: String): Boolean
}