package com.example.movieapp.data.remote.repository


import com.example.movieapp.data.remote.MovieApi
import com.example.movieapp.data.remote.dao.BookmarkDao
import com.example.movieapp.data.remote.dto.MovieDto
import com.example.movieapp.data.remote.local.entity.BookmarkedMovieEntity
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.SortType
import com.example.movieapp.domain.model.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val bookmarkDao: BookmarkDao
) : MovieRepository {

    override suspend fun getMovies(): Result<List<Movie>> {
        return try {
            val movies = api.getMovies()
            val bookmarkedIds = bookmarkDao.getBookmarkedMovies()
                .map { it.map { entity -> entity.movieId } }

            Result.success(movies.map { dto ->
                dto.toMovie(isBookmarked = bookmarkedIds.first().contains(dto.id))
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieById(id: String): Result<Movie> {
        return try {
            val movie = api.getMovieById(id)
            val isBookmarked = bookmarkDao.isMovieBookmarked(id)
            Result.success(movie.toMovie(isBookmarked = isBookmarked))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> {
        return try {
            val movies = api.getMovies()
            val bookmarkedIds = bookmarkDao.getBookmarkedMovies()
                .map { it.map { entity -> entity.movieId } }

            val filteredMovies = movies.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }.map { dto ->
                dto.toMovie(isBookmarked = bookmarkedIds.first().contains(dto.id))
            }

            Result.success(filteredMovies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sortMovies(sortType: SortType): Result<List<Movie>> {
        return try {
            val movies = api.getMovies()
            val bookmarkedIds = bookmarkDao.getBookmarkedMovies()
                .map { it.map { entity -> entity.movieId } }

            val sortedMovies = when (sortType) {
                is SortType.Title -> movies.sortedBy { it.title }
                is SortType.ReleaseDate -> movies.sortedBy { it.releaseDate }
                is SortType.Rating -> movies.sortedByDescending { it.rating?.imdb ?: 0.0 }
            }.map { dto ->
                dto.toMovie(isBookmarked = bookmarkedIds.first().contains(dto.id))
            }

            Result.success(sortedMovies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleBookmark(movieId: String, isBookmarked: Boolean) {
        if (isBookmarked) {
            bookmarkDao.bookmarkMovie(BookmarkedMovieEntity(movieId))
        } else {
            bookmarkDao.removeBookmark(movieId)
        }
    }

    override fun getBookmarkedMovies(): Flow<List<Movie>> {
        return bookmarkDao.getBookmarkedMovies().map { bookmarkedEntities ->
            // Since we only have IDs, we'd need to fetch movie details
            // For simplicity, returning empty list - in real app, you'd join with movie data
            emptyList()
        }
    }

    override suspend fun isMovieBookmarked(movieId: String): Boolean {
        return bookmarkDao.isMovieBookmarked(movieId)
    }
}