package com.alterjuice.task.moviedb.feature.movies.model

import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect
import com.alterjuice.utils.str.Str

sealed interface MoviesEffect: BaseSideEffect {
    data class ShareMovie(val shareDetailsMessage: Str): MoviesEffect
}