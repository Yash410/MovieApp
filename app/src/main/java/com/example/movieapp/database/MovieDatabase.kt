package com.example.movieapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.model.Result

@Database(
    entities = [Result::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun getMovieDao(): MovieDao
}