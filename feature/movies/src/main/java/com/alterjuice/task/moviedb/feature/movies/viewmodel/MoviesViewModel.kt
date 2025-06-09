package com.alterjuice.task.moviedb.feature.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect
import com.alterjuice.task.moviedb.domain.usecase.AddToFavoritesUseCase
import com.alterjuice.task.moviedb.domain.usecase.GetFavoriteMoviesUseCase
import com.alterjuice.task.moviedb.domain.usecase.GetMoviesUseCase
import com.alterjuice.task.moviedb.domain.usecase.RemoveFromFavoritesUseCase
import com.alterjuice.task.moviedb.feature.movies.R
import com.alterjuice.task.moviedb.feature.movies.mappers.toUI
import com.alterjuice.task.moviedb.feature.movies.model.MovieListItem
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEffect
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEvent
import com.alterjuice.task.moviedb.feature.movies.model.MoviesState
import com.alterjuice.task.moviedb.feature.movies.model.MoviesTab
import com.alterjuice.utils.str.StrRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<BaseSideEffect>()
    val effect = _effect.asSharedFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    private val pagerFlow: Flow<PagingData<MovieListItem>> =
        refreshTrigger
            .flatMapLatest {
                getMoviesUseCase()
            }.map { pagingData ->
                val pagingMappedData = pagingData.map { MovieListItem.Movie(it.toUI()) }
                pagingMappedData.insertSeparators { before: MovieListItem.Movie?, after: MovieListItem.Movie? ->
                    when {
                        /* No separators need in end of list */
                        after == null -> null
                        /* Adding separator on start of list */
                        before == null -> MovieListItem.Separator(
                            getFormattedMovieDateSeparator(
                                after.movie.releaseDate
                            )
                        )
                        /* Adding separator if two movies had different month-date */
                        !isMonthYearSame(before.movie.releaseDate, after.movie.releaseDate) -> {
                            MovieListItem.Separator(getFormattedMovieDateSeparator(after.movie.releaseDate))
                        }
                        /* Not adding separator if two movies have same month-date */
                        else -> null
                    }
                }
            }
            .cachedIn(viewModelScope)


    init {
        _state.update { it.copy(movies = pagerFlow) }
        refreshMovies()
        getFavoriteMoviesUseCase().onEach { favoriteMovies ->
            val favorites = favoriteMovies.map { it.toUI() }
            _state.update { it.copy(favoriteMovies = favorites) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MoviesEvent) {
        when (event) {
            is MoviesEvent.AddToFavorites -> addToFavorite(event.movieId)
            MoviesEvent.Refresh -> refreshMovies()
            is MoviesEvent.RemoveFromFavorites -> removeFromFavorites(event.movieId)
            is MoviesEvent.SelectTab -> selectTab(event.tab)
            is MoviesEvent.ShareMovie -> shareMovie(event.movieId, event.title)
        }
    }

    private fun shareMovie(movieId: Int, title: String) {
        viewModelScope.launch {
            val url = StrRes.Text(R.string.tmdb_movie_details_url_path, movieId)
            val shareDetails = StrRes(R.string.share_movie_message_prefix, title, url)
            _effect.emit(MoviesEffect.ShareMovie(shareDetails))
        }
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }

    private fun selectTab(tab: MoviesTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    private fun addToFavorite(movieId: Int) {
        viewModelScope.launch {
            addToFavoritesUseCase(movieId)
            _effect.emit(
                BaseSideEffect.ShowSnackbarEffect(
                    message = StrRes(R.string.event_movie_added_to_favorites)
                )
            )
        }
    }

    private fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch {
            removeFromFavoritesUseCase(movieId)
            _effect.emit(
                BaseSideEffect.ShowSnackbarEffect(
                    message = StrRes(R.string.event_movie_removed_from_favorites)
                )
            )
        }
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        private fun getFormattedMovieDateSeparator(groupDate: LocalDate): String {
            return groupDate.format(formatter)
        }

        private fun isMonthYearSame(date1: LocalDate, date2: LocalDate): Boolean {
            return date1.year == date2.year && date1.month == date2.month
        }
    }
}