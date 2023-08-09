package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.ui.MovieActivity
import com.example.movieapp.ui.MovieViewModel
import com.example.movieapp.utils.Resource
import kotlinx.android.synthetic.main.fragment_popular.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_popular.recycler_view_home

class PopularMoviesFragment: Fragment(R.layout.fragment_popular) {

    lateinit var movieAdapter: MovieAdapter
    lateinit var viewModel: MovieViewModel
    private val TAG = "PopularMoviesFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        movieAdapter.setItemOnClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", it.id)
            }
            findNavController().navigate(R.id.movieFragment, bundle)
        }

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        movieAdapter.differ.submitList(moviesResponse.results.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
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
        recycler_view_home.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}