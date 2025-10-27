package com.example.movieapp.data.remote

import com.example.movieapp.data.remote.dto.MovieDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("movies")
    suspend fun getMovies(): List<MovieDto>

    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") id: String): MovieDto
}