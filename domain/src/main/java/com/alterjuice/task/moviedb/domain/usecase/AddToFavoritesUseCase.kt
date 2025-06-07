package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.repository.MoviesRepository

class AddToFavoritesUseCase(private val repository: MoviesRepository) {
    suspend operator fun invoke(movieID: Int) {
        repository.addToFavourites(movieID)
    }
}