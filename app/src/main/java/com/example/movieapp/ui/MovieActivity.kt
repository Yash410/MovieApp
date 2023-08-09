package com.example.movieapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp.R
import com.example.movieapp.database.MovieDatabase
import com.example.movieapp.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_movie.bottomNavigationView
import kotlinx.android.synthetic.main.activity_movie.moviesNavHostFragment

class MovieActivity : AppCompatActivity() {

    lateinit var viewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieRepository = MovieRepository(MovieDatabase(this))
        val viewModelProviderFactory = MoviesViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)

        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
    }
}