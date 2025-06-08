package com.alterjuice.task.moviedb.domain.repository

import androidx.paging.PagingData
import com.alterjuice.task.moviedb.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<PagingData<Movie>>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun addToFavourites(movieId: Int)
    suspend fun removeFromFavourites(movieId: Int)
}