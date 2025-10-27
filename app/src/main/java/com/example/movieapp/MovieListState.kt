package com.example.movieapp


import com.example.movieapp.domain.model.Movie

data class MovieListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val sortType: MovieSortType = MovieSortType.Title,
    val showSortDialog: Boolean = false
)

sealed class MovieListEvent {
    data class OnSearchQueryChange(val query: String) : MovieListEvent()
    data class Sort(val sortType: MovieSortType) : MovieListEvent()
    data class ToggleBookmark(val movieId: String) : MovieListEvent()
    object ToggleSortDialog : MovieListEvent()
}

sealed class MovieSortType {
    object Title : MovieSortType()
    object ReleaseDate : MovieSortType()
    object Rating : MovieSortType()
}