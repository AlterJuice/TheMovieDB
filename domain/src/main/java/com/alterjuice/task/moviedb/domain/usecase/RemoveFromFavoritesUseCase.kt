package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import javax.inject.Inject


/**
 * Use case for removing a movie from the user's favorites list.
 *
 * This use case handles the logic of removing a movie from favorites by
 * leveraging the [MovieRepository] to persist the change in the local database.
 * It must be executed within a coroutine context due to its suspend nature.
 */
class RemoveFromFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    /**
     * Executes the use case to remove a movie with the specified ID from favorites.
     *
     * @param movieID The unique identifier of the movie to be removed from favorites.
     */
    suspend operator fun invoke(movieID: Int) {
        repository.removeFromFavorites(movieID)
    }
}