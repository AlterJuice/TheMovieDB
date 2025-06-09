package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RemoveFromFavoritesUseCaseTest {

    private val repository: MovieRepository = mockk(relaxed = true)
    private val useCase = RemoveFromFavoritesUseCase(repository)

    @Test
    fun `invoke SHOULD call repository's removeFromFavorites`() = runTest {
        // GIVEN
        val testMovieId = 456

        // WHEN
        useCase(testMovieId)

        // THEN
        coVerify(exactly = 1) { repository.removeFromFavourites(testMovieId) }
    }
}