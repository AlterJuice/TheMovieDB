package com.alterjuice.task.moviedb.domain.usecase

import androidx.paging.PagingData
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a paginated list of movies.
 *
 * This use case fetches movies by accessing the [MovieRepository],
 * which likely queries a remote API or local database. The result is provided as a [Flow]
 * of [PagingData] to support pagination and reactive updates. It should be collected
 * within a coroutine context.
 */
class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    /**
     * Executes the use case to retrieve a paginated list of movies.
     *
     * @return A [Flow] that emits [PagingData] containing [Movie] objects.
     */
    operator fun invoke(): Flow<PagingData<Movie>> = repository.getMovies()
}