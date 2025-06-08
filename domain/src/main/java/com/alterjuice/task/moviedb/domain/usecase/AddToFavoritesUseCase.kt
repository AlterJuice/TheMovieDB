package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(movieID: Int) {
        repository.addToFavourites(movieID)
    }
}