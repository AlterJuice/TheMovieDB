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
        const val DISCOVER_MOVIE_PATH = "discover/movie"
    }

    override suspend fun getMovies(page: Int): MoviesResponseDto {
        return httpClient.get(DISCOVER_MOVIE_PATH) {
            parameter("page", page)
        }.body()
    }
}