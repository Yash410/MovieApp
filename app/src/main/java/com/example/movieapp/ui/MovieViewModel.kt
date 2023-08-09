package com.example.movieapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.Result
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {
    val popularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val searchMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieDetail: MutableLiveData<Resource<MovieDetail>> = MutableLiveData()

    var popularMoviesPage = 1
    var popularMoviesResponse: MovieResponse? = null

    var searchMoviesPage = 1
    var searchMovieResponse: MovieResponse? = null

    init {
        getPopularMovies()
    }


    fun getPopularMovies() = viewModelScope.launch {
        popularMovies.postValue(Resource.Loading())
        val response = movieRepository.getPopularMovies(popularMoviesPage)
        popularMovies.postValue(handlePopularMoviesResponse(response))
    }

    private fun handlePopularMoviesResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                popularMoviesPage++
                if (popularMoviesResponse == null) {
                    popularMoviesResponse = resultResponse
                } else {
                    val oldMovies = popularMoviesResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(popularMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchForMovies(searchQuery: String) = viewModelScope.launch {
        searchMovies.postValue(Resource.Loading())
        val response = movieRepository.searchMovies(searchQuery, searchMoviesPage)
        searchMovies.postValue(handleSearchMoviesResponse(response))
    }

    private fun handleSearchMoviesResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let {resultResponse ->
                if (searchMovieResponse == null) {
                    searchMovieResponse = resultResponse
                } else {
                    val oldMovies = searchMovieResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resource.Success(searchMovieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getMovieDetail(movieId: String) = viewModelScope.launch {
        movieDetail.postValue(Resource.Loading())
        val response = movieRepository.getMovieDetails(movieId)
        movieDetail.postValue(handleMovieDetailResponse(response))
    }

    private fun handleMovieDetailResponse(response: Response<MovieDetail>): Resource<MovieDetail> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveMovie(movie: Result) = viewModelScope.launch {
        movieRepository.upsert(movie)
    }

    fun getSavedMovies() = movieRepository.getSavedMovies()

    fun deleteMovie(movie: Result) = viewModelScope.launch {
        movieRepository.deleteMovie(movie)
    }
}