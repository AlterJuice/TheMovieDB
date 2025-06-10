package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case for adding a movie to the user's favorites list.
 *
 * This use case handles the logic of adding a movie to the favorites list by
 * leveraging the [MovieRepository] to persist the change in the local database.
 * It must be executed within a coroutine context due to its suspend nature.
 */
class AddToFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    /**
     * Executes the use case to add a movie with the specified ID to favorites.
     *
     * @param movieID The unique identifier of the movie to be added to favorites.
     */
    suspend operator fun invoke(movieID: Int) {
        repository.addToFavorites(movieID)
    }
}