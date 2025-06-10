package com.alterjuice.task.moviedb.feature.movies.model

sealed interface MovieListItem {
    data class Movie(val movie: MovieUI) : MovieListItem
    data class Separator(val monthAndYear: String) : MovieListItem
}

internal fun MovieListItem.lazyListKey(): Any = when (this) {
    is MovieListItem.Movie -> movie.id
    is MovieListItem.Separator -> monthAndYear
}