package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddToFavoritesUseCaseTest {
    private val repository: MovieRepository = mockk(relaxed = true)

    private val useCase = AddToFavoritesUseCase(repository)

    @Test
    fun `AddToFavoritesUseCase invoke SHOULD call repository's addToFavorites`() = runTest {
        // GIVEN
        val testMovieId = 123

        // WHEN
        useCase(testMovieId)

        // THEN
        coVerify(exactly = 1) { repository.addToFavourites(testMovieId) }
    }
}