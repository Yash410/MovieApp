package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.Result
import com.example.movieapp.ui.MovieActivity
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Constants.Companion.IMAGE_BASE_URL
import com.example.movieapp.utils.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.fab
import kotlinx.android.synthetic.main.fragment_movie.iv_movie_poster
import kotlinx.android.synthetic.main.fragment_movie.linearLayout
import kotlinx.android.synthetic.main.fragment_movie.movie_budget
import kotlinx.android.synthetic.main.fragment_movie.movie_overview
import kotlinx.android.synthetic.main.fragment_movie.movie_popularity
import kotlinx.android.synthetic.main.fragment_movie.movie_release_date
import kotlinx.android.synthetic.main.fragment_movie.movie_revenue
import kotlinx.android.synthetic.main.fragment_movie.movie_runtime
import kotlinx.android.synthetic.main.fragment_movie.movie_tagline
import kotlinx.android.synthetic.main.fragment_movie.movie_title
import kotlinx.android.synthetic.main.fragment_movie.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_movie.txt_error

class MovieFragment: Fragment(R.layout.fragment_movie) {

    lateinit var movieViewModel: MovieViewModel
    lateinit var movieObserver: Observer<in Resource<MovieDetail>?>
    var movieResult: Result? = null

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
                    txt_error.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        movieViewModel = (activity as MovieActivity).viewModel
        val movieId = arguments?.get("movieId")
        movieId.let {
            movieViewModel.getMovieDetail(movieId.toString())
        }

        fab.setOnClickListener {
            movieResult?.let {
                movieViewModel.saveMovie(it)
                Snackbar.make(view, "Article saved successfully!", Snackbar.LENGTH_SHORT).show()
            }
        }
        movieViewModel.movieDetail.observe(viewLifecycleOwner, movieObserver)
    }

    private fun setViewWithValues(movie: MovieDetail) {
        val movieURl = IMAGE_BASE_URL.plus(movie.poster_path)
        Glide.with(linearLayout).load(movieURl).into(iv_movie_poster)
        movie_title.text = movie.title
        movie_tagline.text = movie.tagline
        movie_release_date.text = movie.release_date
        movie_popularity.text = movie.popularity.toInt().toString()
        movie_runtime.text = movie.runtime.toString()
        movie_budget.text = movie.budget.toString()
        movie_overview.text = movie.overview
        movie_revenue.text = movie.revenue.toString()
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
        paginationProgressBar.visibility = View.INVISIBLE
        fab.show()
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        fab.hide()
        isLoading = true
    }

    var isLoading = false

    override fun onDestroyView() {
        super.onDestroyView()
        movieViewModel.movieDetail.removeObserver(movieObserver)
        movieViewModel.movieDetail.postValue(Resource.Loading())
    }
}