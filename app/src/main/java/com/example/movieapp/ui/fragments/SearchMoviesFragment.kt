package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.ui.MovieActivity
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Constants.Companion.SEARCH_MOVIES_TIME_DELAY
import com.example.movieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_movie_search.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_movie_search.recycler_view_search_movies
import kotlinx.android.synthetic.main.fragment_movie_search.search_movies
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchMoviesFragment: Fragment(R.layout.fragment_movie_search) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        search_movies.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_MOVIES_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForMovies(editable.toString())
                    }
                }
            }
        }

        movieAdapter.setItemOnClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", it.id)
            }
            findNavController().navigate(R.id.movieFragment, bundle)
        }

        viewModel.searchMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        movieAdapter.differ.submitList(moviesResponse.results.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.d(SearchMoviesFragment::class.java.name, response.message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        recycler_view_search_movies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}