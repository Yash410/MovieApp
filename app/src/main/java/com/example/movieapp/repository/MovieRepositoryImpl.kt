package com.example.movieapp.repository

import androidx.lifecycle.LiveData
import com.example.movieapp.api.MoviesAPI
import com.example.movieapp.database.MovieDao
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.Result
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val dao: MovieDao, private val api: MoviesAPI): MovieRepository {
    override suspend fun getPopularMovies(pageNumber: Int): Response<MovieResponse> {
        return api.getPopularMovies(pageNumber)
    }

    override suspend fun searchMovies(
        searchQuery: String,
        pageNumber: Int
    ): Response<MovieResponse> {
        return api.searchForMovies(searchQuery = searchQuery, pageNumber = pageNumber)
    }

    override suspend fun getMovieDetails(movieId: String): Response<MovieDetail> {
        return api.movieDetails(movieId)
    }

    override suspend fun upsert(movie: Result) {
        dao.upsert(movie)
    }

    override fun getSavedMovies(): LiveData<List<Result>> {
       return dao.getAllMovies()
    }

    override suspend fun deleteMovie(movie: Result) {
        dao.delete(movie)
    }
}