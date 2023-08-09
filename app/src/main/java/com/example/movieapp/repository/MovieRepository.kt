package com.example.movieapp.repository

import com.example.movieapp.api.RetrofitInstance
import com.example.movieapp.database.MovieDatabase
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.Result

class MovieRepository(
    private val db: MovieDatabase
) {
    suspend fun getPopularMovies(pageNumber: Int) =
        RetrofitInstance.api.getPopularMovies(pageNumber)

    suspend fun searchMovies(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForMovies(searchQuery = searchQuery, pageNumber = pageNumber)

    suspend fun getMovieDetails(movieId: String) =
        RetrofitInstance.api.movieDetails(movieId)

    suspend fun upsert(movie: Result) = db.getMovieDao().upsert(movie)

    fun getSavedMovies() = db.getMovieDao().getAllMovies()

    suspend fun deleteMovie(movie: Result) = db.getMovieDao().delete(movie)
}