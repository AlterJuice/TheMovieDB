package com.alterjuice.task.moviedb.domain.repository

import com.alterjuice.task.moviedb.domain.model.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun addToFavourites(movieId: Int)
    suspend fun removeFromFavourites(movieId: Int)
}