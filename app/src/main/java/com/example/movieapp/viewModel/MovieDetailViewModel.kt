package com.example.movieapp.viewModel


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId = savedStateHandle.get<String>("movieId") ?: ""

    private val _state = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    init {
        loadMovie()
    }

    fun loadMovie() {
        if (movieId.isBlank()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getMovieById(movieId).fold(
                onSuccess = { movie ->
                    _state.update {
                        it.copy(
                            movie = movie,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load movie details"
                        )
                    }
                }
            )
        }
    }

    fun toggleBookmark(movieId: String) {
        viewModelScope.launch {
            val currentMovie = _state.value.movie
            currentMovie?.let { movie ->
                val newBookmarkState = !movie.isBookmarked
                repository.toggleBookmark(movieId, newBookmarkState)

                _state.update {
                    it.copy(
                        movie = movie.copy(isBookmarked = newBookmarkState)
                    )
                }
            }
        }
    }
}

data class MovieDetailState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)