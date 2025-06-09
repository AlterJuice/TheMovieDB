package com.alterjuice.task.moviedb.repository

import app.cash.turbine.test
import com.alterjuice.task.moviedb.database.AppDatabase
import com.alterjuice.task.moviedb.database.dao.MovieDao
import com.alterjuice.task.moviedb.database.dao.RemoteKeysDao
import com.alterjuice.task.moviedb.database.model.MovieEntity
import com.alterjuice.task.moviedb.network.service.MovieApiService
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class MoviesRepositoryImplTest {
    private lateinit var apiService: MovieApiService
    private lateinit var database: AppDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var remoteKeysDao: RemoteKeysDao


    // Under testing
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk(relaxed = true)
        database = mockk(relaxed = true)
        movieDao = mockk(relaxed = true)
        remoteKeysDao = mockk(relaxed = true)


        every { database.movieDao() } returns movieDao
        every { database.remoteKeysDao() } returns remoteKeysDao


        repository = MovieRepositoryImpl(apiService, database)
    }

    @Test
    fun `addToFavorites SHOULD call dao's updateFavoriteStatus with correct parameters`() = runTest {
        // GIVEN
        val movieId = 123

        // WHEN
        repository.addToFavorites(movieId)

        // THEN
        coVerify(exactly = 1) { movieDao.updateFavoriteStatus(movieId, isFavorite = true) }
    }

    @Test
    fun `removeFromFavorites SHOULD call dao's updateFavoriteStatus with correct parameters`() = runTest {
        // GIVEN
        val movieId = 234

        // WHEN
        repository.removeFromFavorites(movieId)

        // THEN
        coVerify(exactly = 1) { movieDao.updateFavoriteStatus(movieId, isFavorite = false) }
    }

    @Test
    fun `getFavoriteMovies SHOULD return mapped domain models from dao`() = runTest {
        // GIVEN
        val fakeFavoriteEntity = MovieEntity(
            id = 1,
            title = "Test Movie",
            overview = "Overview",
            posterUrl = "someUrl",
            releaseDate = "2025-06-10",
            voteAverage = 8.0,
            voteCount = 100,
            isFavorite = true
        )
        val entityList = listOf(fakeFavoriteEntity)


        every { movieDao.getFavoriteMovies() } returns flowOf(entityList)

        // WHEN
        repository.getFavoriteMovies().test {
            // THEN
            // Awaiting list of entities
            val resultDomainList = awaitItem()

            // Check mapped correctly
            Assert.assertEquals(1, resultDomainList.size)
            val resultMovie = resultDomainList.first()
            Assert.assertEquals("Test Movie", resultMovie.title)
            Assert.assertEquals(true, resultMovie.isFavorite)
            Assert.assertEquals(LocalDate.of(2025, 6, 10), resultMovie.releaseDate)
            Assert.assertEquals(8.0, resultMovie.voteAverage, 0.0)
            Assert.assertEquals("someUrl", resultMovie.posterUrl)


            awaitComplete()
        }
    }
}