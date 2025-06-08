package com.alterjuice.task.moviedb.feature.movies.model

import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect

sealed interface MoviesEffect: BaseSideEffect {
    data class ShareMovie(val movieUrl: String, val movieTitle: String): MoviesEffect
}