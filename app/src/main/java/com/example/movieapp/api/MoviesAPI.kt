package com.example.movieapp.api

import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.Result
import com.example.movieapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("pageNumber")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET("3/search/movie")
    suspend fun searchForMovies(
        @Query("query")
        searchQuery: String,
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("pageNumber")
        pageNumber: Int = 1
    ): Response<MovieResponse>

    @GET("3/movie/{movieId}")
    suspend fun movieDetails(
        @Path("movieId")
        movieId: String,
        @Query("api_key")
        apiKey: String = API_KEY
    ): Response<MovieDetail>
}