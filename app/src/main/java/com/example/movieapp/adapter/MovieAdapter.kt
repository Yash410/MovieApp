package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.Result
import com.example.movieapp.utils.Constants.Companion.IMAGE_BASE_URL
import kotlinx.android.synthetic.main.item_movie_preview.view.cv_iv_movie_poster
import kotlinx.android.synthetic.main.item_movie_preview.view.cv_movie_release_date
import kotlinx.android.synthetic.main.item_movie_preview.view.cv_movie_title

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie_preview, parent, false)
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            val imageURL = IMAGE_BASE_URL.plus(movie.poster_path)
            Glide.with(this).load(imageURL).into(cv_iv_movie_poster)
            cv_movie_title.text = movie.title
            cv_movie_release_date.text = movie.release_date
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