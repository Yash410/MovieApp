package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.Result
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Constants.Companion.IMAGE_BASE_URL
import com.example.movieapp.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment: Fragment(R.layout.fragment_movie) {

    private val movieViewModel by viewModels<MovieViewModel>()
    lateinit var binding: FragmentMovieBinding
    lateinit var movieObserver: Observer<in Resource<MovieDetail>?>
    var movieResult: Result? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieObserver = Observer { movieResponse ->
            when (movieResponse) {
                is Resource.Success -> {
                    hideProgressBar()
                    movieResponse.data?.let { movie ->
                        setViewWithValues(movie)
                        movieResult = movieDetailToMovie(movie)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    binding.txtError.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

                else -> null
            }
        }

        val movieId = arguments?.get("movieId")
        movieId.let {
            movieViewModel.getMovieDetail(movieId.toString())
        }

        binding.fab.setOnClickListener {
            movieResult?.let {
                movieViewModel.saveMovie(it)
                Snackbar.make(view, "Article saved successfully!", Snackbar.LENGTH_SHORT).show()
            }
        }
        movieViewModel.movieDetailLiveData.observe(viewLifecycleOwner, movieObserver)
    }

    private fun setViewWithValues(movie: MovieDetail) {
        val movieURl = IMAGE_BASE_URL.plus(movie.poster_path)
        Glide.with(binding.linearLayout).load(movieURl).into(binding.ivMoviePoster)
        binding.movieTitle.text = movie.title
        binding.movieTagline.text = movie.tagline
        binding.movieReleaseDate.text = movie.release_date
        binding.moviePopularity.text = movie.popularity.toInt().toString()
        binding.movieRuntime.text = movie.runtime.toString()
        binding.movieBudget.text = movie.budget.toString()
        binding.movieOverview.text = movie.overview
        binding.movieRevenue.text = movie.revenue.toString()
    }

    private fun movieDetailToMovie(movieDetail: MovieDetail): Result =
        Result(
            movieDetail.adult,
            movieDetail.backdrop_path,
            movieDetail.id,
            movieDetail.original_language,
            movieDetail.original_title,
            movieDetail.overview,
            movieDetail.popularity,
            movieDetail.poster_path,
            movieDetail.release_date,
            movieDetail.title,
            movieDetail.video,
            movieDetail.vote_average,
            movieDetail.vote_count
        )

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        binding.fab.show()
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        binding.fab.hide()
        isLoading = true
    }

    var isLoading = false
}