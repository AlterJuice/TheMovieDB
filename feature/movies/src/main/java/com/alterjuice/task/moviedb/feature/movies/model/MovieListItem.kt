package com.alterjuice.task.moviedb.feature.movies.model

/**
 * Sealed interface representing different types of items in a movie list, used for displaying
 * movies and date separators in a LazyList.
 */
sealed interface MovieListItem {
    /**
     * Data class representing an individual movie item in the list.
     *
     * @property movie The [MovieUI] object containing movie details.
     */
    data class Movie(val movie: MovieUI) : MovieListItem

    /**
     * Data class representing a time-based separator in the movie list (e.g., "January 2023").
     *
     * @property monthAndYear A formatted string containing the month and year for the separator.
     */
    data class Separator(val monthAndYear: String) : MovieListItem
}

/**
 * Generates a stable unique key for each item in a LazyList.
 *
 * For [Movie] items, uses the movie ID. For [Separator] items, uses the stored month/year string.
 * This helps Compose efficiently track item identities during list recomposition.
 */
internal fun MovieListItem.lazyListKey(): Any = when (this) {
    is MovieListItem.Movie -> movie.id.toString()
    is MovieListItem.Separator -> monthAndYear
}