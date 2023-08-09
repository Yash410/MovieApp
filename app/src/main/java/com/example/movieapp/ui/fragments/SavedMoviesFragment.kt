package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.ui.MovieActivity
import com.example.movieapp.ui.MovieViewModel
import kotlinx.android.synthetic.main.fragment_saved_movies.recycler_view_saved_movies
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.model.MovieDetail
import com.example.movieapp.model.Result
import com.google.android.material.snackbar.Snackbar

class SavedMoviesFragment: Fragment(R.layout.fragment_saved_movies) {

    lateinit var movieViewModel: MovieViewModel
    lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = (activity as MovieActivity).viewModel
        setupRecyclerView()

        movieAdapter.setItemOnClickListener {
            val bundle = Bundle().apply {
                putInt("movieId", it.id)
            }
            findNavController().navigate(R.id.movieFragment, bundle)
        }

        movieViewModel.getSavedMovies().observe(viewLifecycleOwner, Observer { movies ->
            movieAdapter.differ.submitList(movies)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = movieAdapter.differ.currentList[position]
                movieViewModel.deleteMovie(movie)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        movieViewModel.saveMovie(movie)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recycler_view_saved_movies)
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter()
        recycler_view_saved_movies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}