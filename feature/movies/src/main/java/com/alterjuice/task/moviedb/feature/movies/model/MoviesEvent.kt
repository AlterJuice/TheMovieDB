package com.alterjuice.task.moviedb.feature.movies.model


/**
 * Sealed interface representing user-initiated events in the movies feature.
 * These events drive state changes or side effects.
 */
sealed interface MoviesEvent {

    /**
     * Event to trigger a refresh of movie data, typically initiated by the user
     * or automatically after a state change.
     */
    object Refresh : MoviesEvent

    /**
     * Data class for the event of adding a movie to the user's favorites.
     *
     * @property movieId The unique identifier of the movie to be added.
     */
    data class AddToFavorites(val movieId: Int) : MoviesEvent

    /**
     * Data class for the event of removing a movie from the user's favorites.
     *
     * @property movieId The unique identifier of the movie to be removed.
     */
    data class RemoveFromFavorites(val movieId: Int) : MoviesEvent

    /**
     * Data class for the event of sharing a movie.
     *
     * @property movieId The unique identifier of the movie.
     * @property title The title of the movie to be included in the shared content.
     */
    data class ShareMovie(val movieId: Int, val title: String): MoviesEvent

    /**
     * Data class for the event of selecting a tab in the movies UI.
     *
     * @property tab The [MoviesTab] representing the user's selected tab option.
     */
    data class SelectTab(val tab: MoviesTab) : MoviesEvent
    
    /**
     * Data class for the event of an error occurring during a paging operation.
     *
     * @property error The [Throwable] that occurred during the paging process.
     */
    data class PagingError(val error: Throwable): MoviesEvent
}