package com.alterjuice.task.moviedb.feature.movies.model

import androidx.paging.PagingData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MoviesState(
    val movies: Flow<PagingData<MovieListItem>> = emptyFlow(),
    val favoriteMovies: ImmutableList<MovieListItem> = persistentListOf(),
    val selectedTab: MoviesTab = MoviesTab.ALL
)