package com.alterjuice.task.moviedb.feature.movies.model

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.alterjuice.utils.str.Str
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * State container for the movies feature, holding the current movie data, favorite movies,
 * and the selected tab.
 */
@Immutable
data class MoviesState(
    /** Flow emitting the current paging data for movies. Defaults to empty. */
    val movies: Flow<PagingData<MovieListItem>> = emptyFlow(),
    /** Immutable list of favorite movies. Defaults to empty. */
    val favoriteMovies: ImmutableList<MovieListItem> = persistentListOf(),
    /** Currently selected tab. Defaults to [MoviesTab.ALL]. */
    val selectedTab: MoviesTab = MoviesTab.ALL,
    /** Str-object describing paging error; Defaults to null */
    val pagingError: Str? = null
)