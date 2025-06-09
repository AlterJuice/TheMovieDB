package com.alterjuice.task.moviedb.domain.usecase

import androidx.paging.PagingData
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlin.test.Test

class GetMoviesUseCaseTest {
    @Test
    fun `invoke SHOULD call repository's getMovies method`() {
        // GIVEN
        val repository: MovieRepository = mockk()
        val useCase = GetMoviesUseCase(repository)

        every { repository.getMovies() } returns emptyFlow<PagingData<Movie>>()

        // WHEN
        useCase()

        // THEN
        // Check repository.getMovies() was invoked 1 time
        verify(exactly = 1) { repository.getMovies() }
    }
}