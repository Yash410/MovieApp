package com.example.movieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.model.Result
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {
    private val _popularMoviesLiveData: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val popularMoviesLiveData: LiveData<Resource<MovieResponse>>
        get() = _popularMoviesLiveData

    private val _searchMoviesLiveData: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val searchMoviesLiveData: LiveData<Resource<MovieResponse>>
        get() = _searchMoviesLiveData

    private val _movieDetailLiveData: MutableLiveData<Resource<MovieDetail>> = MutableLiveData()
    val movieDetailLiveData: LiveData<Resource<MovieDetail>>
        get() = _movieDetailLiveData

    private var popularMoviesPage = 1
    private var popularMoviesResponse: MovieResponse? = null

    private var searchMoviesPage = 1
    private var searchMovieResponse: MovieResponse? = null

    init {
        getPopularMovies()
    }


    fun getPopularMovies() = viewModelScope.launch {
        _popularMoviesLiveData.postValue(Resource.Loading())
        val response = movieRepository.getPopularMovies(popularMoviesPage)
        _popularMoviesLiveData.postValue(handlePopularMoviesResponse(response))
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
        _searchMoviesLiveData.postValue(Resource.Loading())
        val response = movieRepository.searchMovies(searchQuery, searchMoviesPage)
        _searchMoviesLiveData.postValue(handleSearchMoviesResponse(response))
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
        _movieDetailLiveData.postValue(Resource.Loading())
        val response = movieRepository.getMovieDetails(movieId)
        _movieDetailLiveData.postValue(handleMovieDetailResponse(response))
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