package com.alterjuice.task.moviedb.domain.repository

import androidx.paging.PagingData
import com.alterjuice.task.moviedb.domain.model.Movie
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<PagingData<Movie>>
    fun getFavoriteMovies(): Flow<ImmutableList<Movie>>
    suspend fun addToFavorites(movieId: Int)
    suspend fun removeFromFavorites(movieId: Int)
}