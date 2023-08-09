package com.example.movieapp.model

data class MovieResponse(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)