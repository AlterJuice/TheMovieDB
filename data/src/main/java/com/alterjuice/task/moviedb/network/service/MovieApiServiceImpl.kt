package com.alterjuice.task.moviedb.network.service

import com.alterjuice.task.moviedb.network.model.MoviesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Implementation of [MovieApiService] that interacts with the Movie API using a Ktor [HttpClient].
 * Handles the network request to fetch a paginated list of movies with predefined filtering and sorting parameters.
 */
internal class MovieApiServiceImpl(
    private val httpClient: HttpClient
) : MovieApiService {
    companion object {
        /**
         * The API endpoint path for discovering movies.
         * Corresponds to "discover/movie" in the REST API.
         */
        private const val DISCOVER_MOVIE_PATH = "discover/movie"

        /**
         * Query parameter for filtering movies with a vote average greater than or equal to a specified value.
         * Default value is 7.0.
         */
        private const val VOTE_AVERAGE_PARAM = "vote_average.gte"

        /**
         * Query parameter for filtering movies with a vote count greater than or equal to a specified value.
         * Default value is 100.
         */
        private const val VOTE_COUNT_PARAM = "vote_count.gte"

        /**
         * Query parameter for defining the sorting order of the results.
         * Default is sorted by primary release date in descending order.
         */
        private const val SORT_BY_PARAM = "sort_by"

        /**
         * Query parameter for specifying the page number of the paginated results.
         * (1-indexed as per API requirements)
         */
        private const val PAGE_PARAM = "page"
    }

    /**
     * Fetches a paginated list of movies from the API with predefined filtering and sorting.
     *
     * @param page The page number to retrieve (1-indexed)
     * @return A [MoviesResponseDto] containing the list of movies and pagination metadata
     */
    override suspend fun getMovies(page: Int): MoviesResponseDto {
        return httpClient.get(DISCOVER_MOVIE_PATH) {
            parameter(PAGE_PARAM, page)
            parameter(VOTE_AVERAGE_PARAM, 7)
            parameter(VOTE_COUNT_PARAM, 100)
            parameter(SORT_BY_PARAM, "primary_release_date.desc")
        }.body()
    }
}