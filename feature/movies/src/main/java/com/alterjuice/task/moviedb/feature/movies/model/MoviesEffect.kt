package com.alterjuice.task.moviedb.feature.movies.model

import com.alterjuice.utils.str.Str

sealed interface MoviesEffect {
    data class ShowSnackbar(val message: Str) : MoviesEffect
}