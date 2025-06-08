package com.alterjuice.task.moviedb.feature.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.usecase.AddToFavoritesUseCase
import com.alterjuice.task.moviedb.domain.usecase.GetMoviesUseCase
import com.alterjuice.task.moviedb.domain.usecase.RemoveFromFavoritesUseCase
import com.alterjuice.task.moviedb.feature.movies.mappers.toUI
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEffect
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEvent
import com.alterjuice.task.moviedb.feature.movies.model.MoviesState
import com.alterjuice.utils.str.StrRaw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    getMoviesUseCase: GetMoviesUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MoviesState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MoviesEffect>()
    val effect = _effect.asSharedFlow()

    init {
        _state.update {
            it.copy(movies = getMoviesUseCase().map { pagingData ->
                pagingData.map { it.toUI() }
            })
        }
    }

    fun onEvent(event: MoviesEvent) {
        when (event) {
            is MoviesEvent.ChangeFavoriteStatus -> onChangeFavoriteStatus(event.movie)
        }
    }

    private fun onChangeFavoriteStatus(movie: Movie) {
        viewModelScope.launch {
            if (movie.isFavorite) {
                removeFromFavoritesUseCase(movie.id)
                _effect.emit(MoviesEffect.ShowSnackbar(StrRaw("Removed from favorites")))
            } else {
                addToFavoritesUseCase(movie.id)
                _effect.emit(MoviesEffect.ShowSnackbar(StrRaw("Added to favorites")))
            }
        }
    }
}