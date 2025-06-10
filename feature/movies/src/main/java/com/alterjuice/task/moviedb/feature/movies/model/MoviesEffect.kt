package com.alterjuice.task.moviedb.feature.movies.model

import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect
import com.alterjuice.utils.str.Str

/**
 * Sealed interface representing possible side effects in the movies feature.
 * These effects handle actions that require UI interaction or external operations.
 */
sealed interface MoviesEffect: BaseSideEffect {
    /**
     * Data class for triggering movie sharing functionality.
     *
     * @property shareDetailsMessage The message content to be shared, formatted using [Str] for localized/text handling.
     */
    data class ShareMovie(val shareDetailsMessage: Str): MoviesEffect
}