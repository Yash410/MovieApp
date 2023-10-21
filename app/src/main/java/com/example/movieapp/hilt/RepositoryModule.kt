package com.example.movieapp.hilt

import com.example.movieapp.api.MoviesAPI
import com.example.movieapp.database.MovieDao
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    fun objectForCartRepositoryInterface(@Singleton movieDao: MovieDao, @Singleton moviesAPI: MoviesAPI): MovieRepository {
        return MovieRepositoryImpl(movieDao, moviesAPI)
    }
}