package com.alterjuice.task.moviedb.di

import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import com.alterjuice.task.moviedb.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface IRepositoryModule {
    @Binds
    @Singleton
    fun bindsMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}
