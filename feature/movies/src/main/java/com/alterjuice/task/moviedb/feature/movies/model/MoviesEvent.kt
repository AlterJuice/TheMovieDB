package com.alterjuice.task.moviedb.feature.movies.model

import com.alterjuice.task.moviedb.domain.model.Movie


sealed interface MoviesEvent {
    data class ChangeFavoriteStatus(val movie: Movie) : MoviesEvent
}