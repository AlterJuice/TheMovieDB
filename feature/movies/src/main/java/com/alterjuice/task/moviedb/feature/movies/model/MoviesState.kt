package com.alterjuice.task.moviedb.feature.movies.model

import androidx.paging.PagingData
import com.alterjuice.task.moviedb.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MoviesState(
    val movies: Flow<PagingData<MovieListItem>> = emptyFlow(),
    val favoriteMovies: List<MovieUI> = emptyList(),
    val selectedTab: MoviesTab = MoviesTab.ALL
)