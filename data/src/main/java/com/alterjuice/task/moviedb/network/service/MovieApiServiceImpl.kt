package com.alterjuice.task.moviedb.network.service

import com.alterjuice.task.moviedb.network.model.MoviesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class MovieApiServiceImpl(
    private val httpClient: HttpClient
) : MovieApiService {
    companion object {
        private const val DISCOVER_MOVIE_PATH = "discover/movie"
        private const val VOTE_AVERAGE_PARAM = "vote_average.gte"
        private const val VOTE_COUNT_PARAM = "vote_count.gte"
        private const val SORT_BY_PARAM = "sort_by"
        private const val PAGE_PARAM = "page"

    }

    override suspend fun getMovies(page: Int): MoviesResponseDto {
        return httpClient.get(DISCOVER_MOVIE_PATH) {
            parameter(PAGE_PARAM, page)
            parameter(VOTE_AVERAGE_PARAM, 7)
            parameter(VOTE_COUNT_PARAM, 100)
            parameter(SORT_BY_PARAM, "primary_release_date.desc")
        }.body()
    }
}