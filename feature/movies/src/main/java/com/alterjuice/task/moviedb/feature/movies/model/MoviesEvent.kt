package com.alterjuice.task.moviedb.feature.movies.model


sealed interface MoviesEvent {
    object Refresh : MoviesEvent
    data class AddToFavorites(val movieId: Int) : MoviesEvent
    data class RemoveFromFavorites(val movieId: Int) : MoviesEvent
    data class SelectTab(val tab: MoviesTab) : MoviesEvent
}