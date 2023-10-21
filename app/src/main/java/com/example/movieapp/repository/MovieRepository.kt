package com.example.movieapp.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.Result
import retrofit2.Response

interface MovieRepository {

    suspend fun getPopularMovies(pageNumber: Int): Response<MovieResponse>

    suspend fun getMovieDetails(movieId: String): Response<MovieDetail>

    suspend fun upsert(movie: Result)

    fun getSavedMovies(): LiveData<List<Result>>

    suspend fun deleteMovie(movie: Result)

    suspend fun searchMovies(searchQuery: String, pageNumber: Int): Response<MovieResponse>
}
