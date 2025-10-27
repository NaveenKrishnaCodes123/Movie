package com.example.movieapp.data.remote.dto


import com.example.movieapp.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("release_date")
    val releaseDate: Long,

    @SerializedName("rating")
    val rating: RatingDto?,

    @SerializedName("poster_url")
    val posterUrl: String,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("director")
    val director: String? = null,

    @SerializedName("genre")
    val genre: List<String>? = emptyList(),

    @SerializedName("cast")
    val cast: List<String>? = emptyList(),

    @SerializedName("duration")
    val duration: Int? = null
) {
    fun toMovie(isBookmarked: Boolean = false): Movie {
        return Movie(
            id = id,
            title = title,
            release_date = releaseDate,
            rating = rating?.imdb ?: 0.0,
            poster_url = posterUrl,
            overview = overview,
            genre = genre,
            cast = cast,
            duration = duration,
            isBookmarked = isBookmarked,
            description = description.toString(),
            director = director.toString()
        )
    }
}

data class RatingDto(
    @SerializedName("imdb")
    val imdb: Double
)