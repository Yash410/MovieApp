package com.example.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.MovieAdapter
import com.example.movieapp.databinding.FragmentSavedMoviesBinding
import com.example.movieapp.ui.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedMoviesFragment: Fragment(R.layout.fragment_saved_movies) {

    private val movieViewModel by viewModels<MovieViewModel>()
    @Inject lateinit var movieAdapter: MovieAdapter
    lateinit var binding: FragmentSavedMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            attachToRecyclerView(binding.recyclerViewSavedMovies)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewSavedMovies.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}