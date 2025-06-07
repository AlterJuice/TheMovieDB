package com.alterjuice.task.moviedb.network.service

import com.alterjuice.task.moviedb.network.model.MoviesResponseDto

interface MovieApiService {
    suspend fun getMovies(page: Int): MoviesResponseDto
}