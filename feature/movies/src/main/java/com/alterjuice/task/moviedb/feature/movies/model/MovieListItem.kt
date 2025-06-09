package com.alterjuice.task.moviedb.feature.movies.model

sealed interface MovieListItem {
    data class Movie(val movie: MovieUI) : MovieListItem
    data class Separator(val monthAndYear: String) : MovieListItem
}