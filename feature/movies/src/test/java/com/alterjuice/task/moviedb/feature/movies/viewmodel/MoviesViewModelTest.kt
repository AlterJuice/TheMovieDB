package com.alterjuice.task.moviedb.feature.movies.viewmodel

import androidx.paging.PagingData
import app.cash.turbine.test
import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.usecase.AddToFavoritesUseCase
import com.alterjuice.task.moviedb.domain.usecase.GetFavoriteMoviesUseCase
import com.alterjuice.task.moviedb.domain.usecase.GetMoviesUseCase
import com.alterjuice.task.moviedb.domain.usecase.RemoveFromFavoritesUseCase
import com.alterjuice.task.moviedb.feature.movies.R
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEffect
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEvent
import com.alterjuice.task.moviedb.feature.movies.model.MoviesTab
import com.alterjuice.utils.str.StrRes
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getMoviesUseCase: GetMoviesUseCase = mockk()
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase = mockk()
    private val addToFavoritesUseCase: AddToFavoritesUseCase = mockk(relaxed = true)
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase = mockk(relaxed = true)

    // Testable view model
    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { getMoviesUseCase() } returns emptyFlow<PagingData<Movie>>()
        every { getFavoriteMoviesUseCase() } returns emptyFlow<List<Movie>>()

        viewModel = createViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // GIVEN
        // Just created ViewModel in setUp()

        // WHEN

        // THEN:
        val initialState = viewModel.state.value
        Assert.assertEquals(MoviesTab.ALL, initialState.selectedTab)
        Assert.assertTrue(initialState.favoriteMovies.isEmpty())
    }

    @Test
    fun `event SelectTab SHOULD update selectedTab in state`() = runTest {
        // GIVEN
        val newTab = MoviesTab.FAVORITES
        val event = MoviesEvent.SelectTab(newTab)

        viewModel.state.test {
            awaitItem()

            // WHEN
            viewModel.onEvent(event)

            // THEN
            val updatedState = awaitItem()
            Assert.assertEquals(newTab, updatedState.selectedTab)
        }
    }

    @Test
    fun `event RemoveFromFavorites SHOULD call usecase and emit snackbar effect`() = runTest {
        // GIVEN
        val movieId = 456
        val event = MoviesEvent.RemoveFromFavorites(movieId)
        val expectedMessage = StrRes(R.string.event_movie_removed_from_favorites)
        val idSlot = slot<Int>()

        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            // Check UseCase was invoked with correct movieID
            coVerify(exactly = 1) { removeFromFavoritesUseCase(capture(idSlot)) }
            Assert.assertEquals(movieId, idSlot.captured)

            // Check the ShowSnackbarEffect effect was sent
            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is BaseSideEffect.ShowSnackbarEffect)

            // Check message equals to expected
            val actualEffect = emittedEffect as BaseSideEffect.ShowSnackbarEffect
            Assert.assertEquals(expectedMessage, actualEffect.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `event AddToFavorites SHOULD call usecase and emit snackbar effect`() = runTest {
        // GIVEN
        val movieId = 456
        val event = MoviesEvent.AddToFavorites(movieId)
        val expectedMessage = StrRes(R.string.event_movie_added_to_favorites)
        val idSlot = slot<Int>()

        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            // Check UseCase was invoked with correct movieID
            coVerify(exactly = 1) { addToFavoritesUseCase(capture(idSlot)) }
            Assert.assertEquals(movieId, idSlot.captured)

            // Check the ShowSnackbarEffect effect was sent
            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is BaseSideEffect.ShowSnackbarEffect)

            // Check message equals to expected
            val actualEffect = emittedEffect as BaseSideEffect.ShowSnackbarEffect
            Assert.assertEquals(expectedMessage, actualEffect.message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `WHEN favorites flow from usecase emits new list THEN state SHOULD be updated`() = runTest {
        // GIVEN
        val favoritesFlow = MutableStateFlow<List<Movie>>(emptyList())
        every { getFavoriteMoviesUseCase() } returns favoritesFlow

        viewModel = createViewModel()

        viewModel.state.test {
            Assert.assertEquals(true, awaitItem().favoriteMovies.isEmpty())

            // WHEN
            val newFavorites =
                listOf(Movie(1, "Favorite Movie", "", null, LocalDate.now(), isFavorite = true))
            favoritesFlow.emit(newFavorites)

            // THEN
            val updatedState = awaitItem()
            Assert.assertEquals(1, updatedState.favoriteMovies.size)
            Assert.assertEquals("Favorite Movie", updatedState.favoriteMovies.first().title)
        }
    }

    @Test
    fun `event Refresh SHOULD re-trigger getMoviesUseCase`() = runTest {
        // GIVEN
        // Viewmodel created in setUp and getMoviesUseCase() was invoked once inside MoviesViewModel::init.
        val event = MoviesEvent.Refresh

        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.value.movies.collect {}
        }

        // WHEN
        viewModel.onEvent(event)
        advanceUntilIdle()

        // THEN
        // Check getMoviesUseCase was invoked the second time.
        verify(exactly = 2) { getMoviesUseCase() }
        job.cancel()
    }

    @Test
    fun `event ShareMovie SHOULD emit ShareMovie effect`() = runTest {
        // GIVEN
        val movieId = 456
        val movieTitle = "Test movie"

        val expectedMovieUrl = StrRes.Text(R.string.tmdb_movie_details_url_path, movieId)
        val expectedShareMessage = StrRes(R.string.share_movie_message_prefix, movieTitle, expectedMovieUrl)

        val event = MoviesEvent.ShareMovie(movieId, movieTitle)
        println("Expected: $expectedShareMessage")
        viewModel.effect.test {
            // WHEN
            viewModel.onEvent(event)

            // THEN
            val emittedEffect = awaitItem()
            Assert.assertTrue(emittedEffect is MoviesEffect.ShareMovie)

            val shareDetailsMessage = (emittedEffect as MoviesEffect.ShareMovie).shareDetailsMessage
            println("Received: $shareDetailsMessage")
            Assert.assertEquals(shareDetailsMessage, expectedShareMessage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel() = MoviesViewModel(
        getMoviesUseCase,
        getFavoriteMoviesUseCase,
        addToFavoritesUseCase,
        removeFromFavoritesUseCase
    )
}