package com.alterjuice.task.moviedb.network.service

import com.alterjuice.task.moviedb.network.model.MoviesResponseDto

/**
 * Internal service interface for interacting with the Movie API.
 * Provides methods to retrieve movie data from the network.
 */
internal interface MovieApiService {
    /**
     * Fetches a paginated list of movies from the API.
     *
     * @param page The page number to retrieve (1-indexed)
     * @return A [MoviesResponseDto] containing the list of movies and pagination metadata
     */
    suspend fun getMovies(page: Int): MoviesResponseDto
}