package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the user's favorite movies.
 *
 * This use case fetches the list of favorite movies by accessing the [MovieRepository],
 * which likely queries a local database. The result is provided as a [Flow] allowing
 * observers to react to changes in the favorites list over time.
 * It should be collected within a coroutine context.
 */
class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    /**
     * Executes the use case to retrieve the current list of favorite movies.
     *
     * @return A [Flow] that emits an [ImmutableList] of [Movie] objects representing the user's favorites.
     */
    operator fun invoke(): Flow<ImmutableList<Movie>> = repository.getFavoriteMovies()
}