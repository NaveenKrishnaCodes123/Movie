package com.example.movieapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.MovieListEvent
import com.example.movieapp.MovieListState
import com.example.movieapp.MovieSortType
import com.example.movieapp.domain.model.SortType
import com.example.movieapp.domain.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    init {
        loadMovies()
    }

    fun onEvent(event: MovieListEvent) {
        when (event) {
            is MovieListEvent.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = event.query) }
                searchMovies(event.query)
            }

            is MovieListEvent.Sort -> {
                _state.update { it.copy(sortType = event.sortType) }
                sortMovies(event.sortType)
            }

            is MovieListEvent.ToggleBookmark -> {
                toggleBookmark(event.movieId)
            }

            MovieListEvent.ToggleSortDialog -> {
                _state.update { it.copy(showSortDialog = !it.showSortDialog) }
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getMovies().fold(
                onSuccess = { movies ->
                    _state.update {
                        it.copy(
                            movies = movies,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadMovies()
            } else {
                repository.searchMovies(query).fold(
                    onSuccess = { movies ->
                        _state.update { it.copy(movies = movies) }
                    },
                    onFailure = { error ->
                        _state.update { it.copy(error = error.message ?: "Search failed") }
                    }
                )
            }
        }
    }

    private fun sortMovies(sortType: MovieSortType) {
        viewModelScope.launch {
            val domainSortType = when (sortType) {
                MovieSortType.Title -> SortType.Title
                MovieSortType.ReleaseDate -> SortType.ReleaseDate
                MovieSortType.Rating -> SortType.Rating
            }

            repository.sortMovies(domainSortType).fold(
                onSuccess = { movies ->
                    _state.update { it.copy(movies = movies, showSortDialog = false) }
                },
                onFailure = { error ->
                    _state.update { it.copy(error = error.message ?: "Sort failed") }
                }
            )
        }
    }

    private fun toggleBookmark(movieId: String) {
        viewModelScope.launch {
            val currentMovies = _state.value.movies
            val movie = currentMovies.find { it.id == movieId }
            movie?.let {
                val newBookmarkState = !it.isBookmarked
                repository.toggleBookmark(movieId, newBookmarkState)

                // Update local state
                val updatedMovies = currentMovies.map { m ->
                    if (m.id == movieId) m.copy(isBookmarked = newBookmarkState) else m
                }
                _state.update { it.copy(movies = updatedMovies) }
            }
        }
    }
}