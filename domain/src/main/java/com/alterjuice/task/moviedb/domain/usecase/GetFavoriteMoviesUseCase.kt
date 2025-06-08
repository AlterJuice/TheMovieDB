package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getFavoriteMovies()
}