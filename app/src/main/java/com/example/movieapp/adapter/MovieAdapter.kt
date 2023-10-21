package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemMoviePreviewBinding
import com.example.movieapp.model.Result
import com.example.movieapp.utils.Constants.Companion.IMAGE_BASE_URL
import javax.inject.Inject

class MovieAdapter @Inject constructor(): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemMoviePreviewBinding: ItemMoviePreviewBinding): RecyclerView.ViewHolder(itemMoviePreviewBinding.root) {
        val binding: ItemMoviePreviewBinding = itemMoviePreviewBinding
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            ItemMoviePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            val imageURL = IMAGE_BASE_URL.plus(movie.poster_path)
            Glide.with(this).load(imageURL).into(holder.binding.cvIvMoviePoster)
            holder.binding.cvMovieTitle.text = movie.title
            holder.binding.cvMovieReleaseDate.text = movie.release_date
            setOnClickListener {
                onItemClickListener?.let {
                    it(movie)
                }
            }
        }
    }

    private var onItemClickListener: ((Result) -> Unit) ? = null

    fun setItemOnClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }
}