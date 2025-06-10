package com.alterjuice.task.moviedb.domain.repository

import androidx.paging.PagingData
import com.alterjuice.task.moviedb.domain.model.Movie
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for movie-related data operations. Provides methods to fetch movies, manage favorites,
 * and handle pagination using a remote mediator.
 */
interface MovieRepository {
    /**
     * Returns a flow of paging data containing movies loaded from the remote and local data sources.
     * Uses a remote mediator to handle the pagination and caching.
     */
    fun getMovies(): Flow<PagingData<Movie>>

    /**
     * Returns a flow containing an immutable list of favorite movies stored locally.
     */
    fun getFavoriteMovies(): Flow<ImmutableList<Movie>>

    /**
     * Adds a movie with the specified [movieId] to the favorites in the local database.
     * This is a suspend function and should be called from a coroutine.
     */
    suspend fun addToFavorites(movieId: Int)

    /**
     * Removes a movie with the specified [movieId] from the favorites in the local database.
     * This is a suspend function and should be called from a coroutine.
     */
    suspend fun removeFromFavorites(movieId: Int)
}