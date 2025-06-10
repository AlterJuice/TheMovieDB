package com.alterjuice.task.moviedb.domain.usecase

import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class GetFavoriteMoviesUseCaseTest {


    @Test
    fun `GetFavoriteMoviesUseCase invoke SHOULD call repository and return its data`() {
        // GIVEN
        val repository: MovieRepository = mockk(relaxed = true)
        val useCase = GetFavoriteMoviesUseCase(repository)
        val fakeFavoriteMovie = Movie(
            id = 1,
            title = "Favorite Movie",
            overview = "Overview",
            posterUrl = null,
            releaseDate = LocalDate.MIN,
            voteAverage = 4.3,
            voteCount = 567,
            isFavorite = true
        )
        val fakeFavorites = persistentListOf(fakeFavoriteMovie)
        val expectedFlow = flowOf(fakeFavorites)

        every { repository.getFavoriteMovies() } returns expectedFlow

        // WHEN
        val actualFlow = useCase()

        // THEN
        verify(exactly = 1) { repository.getFavoriteMovies() }
        Assert.assertEquals(expectedFlow, actualFlow)
    }
}