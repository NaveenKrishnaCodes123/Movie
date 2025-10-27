package com.example.movieapp.domain.model


data class Movie(
    val id: String,
    val title: String,
    val release_date: Long,
    val rating: Double,
    val poster_url: String,
    val overview: String? = null,
    val genre: List<String>? = emptyList(),
    val duration: Int? = null,
    val isBookmarked: Boolean = false,
    val description: String?=null,
    val director: String?=null,
    val cast: List<String>? = emptyList(),

)
data class RatingDto(
    val imdb: Double
)
sealed class SortType {
    object Title : SortType()
    object ReleaseDate : SortType()
    object Rating : SortType()
}
