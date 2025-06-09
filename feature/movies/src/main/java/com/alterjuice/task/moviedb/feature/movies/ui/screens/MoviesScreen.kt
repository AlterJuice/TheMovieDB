@file:OptIn(ExperimentalContracts::class)

package com.alterjuice.task.moviedb.feature.movies.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.alterjuice.task.moviedb.core.ui.components.BaseScreen
import com.alterjuice.task.moviedb.core.ui.components.HorizontallyAnimatedContent
import com.alterjuice.task.moviedb.core.ui.components.rememberSnackbarEffectHandler
import com.alterjuice.task.moviedb.core.ui.extensions.pagedItems
import com.alterjuice.task.moviedb.core.ui.utils.EffectsCollector
import com.alterjuice.task.moviedb.feature.movies.model.MovieUI
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEvent
import com.alterjuice.task.moviedb.feature.movies.model.MoviesTab
import com.alterjuice.task.moviedb.feature.movies.ui.components.MovieCard
import com.alterjuice.task.moviedb.feature.movies.ui.components.MovieTabsComponent
import com.alterjuice.task.moviedb.feature.movies.ui.components.MoviesLazyList
import com.alterjuice.task.moviedb.feature.movies.ui.utils.rememberShareEffectHandler
import com.alterjuice.task.moviedb.feature.movies.viewmodel.MoviesViewModel
import kotlin.contracts.ExperimentalContracts


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier,
    vm: MoviesViewModel = hiltViewModel<MoviesViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessageEffectHandler = rememberSnackbarEffectHandler(snackbarHostState)
    val shareMovieEffectHandler = rememberShareEffectHandler()
    EffectsCollector(
        effects = vm.effect,
        snackbarMessageEffectHandler,
        shareMovieEffectHandler
    )
    BaseScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        topBar = {
            MediumTopAppBar(
                modifier = Modifier,
                title = {},
                navigationIcon = {},
                actions = {},
                collapsedHeight = 0.dp,
                expandedHeight = 10.dp,
                colors = TopAppBarDefaults.mediumTopAppBarColors(),
                scrollBehavior = null
            )
        },
    ) { paddings ->
        val tabs = remember { MoviesTab.entries }
        val state = vm.state.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier.padding(paddings)
        ) {
            MovieTabsComponent(
                modifier = Modifier.fillMaxWidth(),
                tabs = tabs,
                selectedTab = state.value.selectedTab,
                onTabSelected = { vm.onEvent(MoviesEvent.SelectTab(it)) }
            )
            @Composable fun MovieCardContent(movie: MovieUI) {
                MovieCard(
                    modifier = Modifier,
                    movie = movie,
                    onAddToFavourite = { vm.onEvent(MoviesEvent.AddToFavorites(movie.id)) },
                    onRemoveFromFavourite = { vm.onEvent(MoviesEvent.RemoveFromFavorites(movie.id)) },
                    onShare = { vm.onEvent(MoviesEvent.ShareMovie(movie.id, movie.title)) },
                )
            }
            val movies = state.value.movies.collectAsLazyPagingItems()
            val allMoviesState = rememberLazyListState()
            val favoriteMoviesState = rememberLazyListState()
            HorizontallyAnimatedContent(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
                targetState = state.value.selectedTab,
                stateToOrderIndex = { it.ordinal }
            ) { target ->
                when (target) {
                    MoviesTab.ALL -> {
                        MoviesLazyList(
                            modifier = Modifier,
                            state = allMoviesState
                        ) {
                            pagedItems(
                                items = movies,
                                key = { index -> movies.peek(index)?.id?: index },
                                itemContent = { MovieCardContent(it) }
                            )
                        }
                    }
                    MoviesTab.FAVORITES -> {
                        MoviesLazyList(
                            modifier = Modifier,
                            state = favoriteMoviesState
                        ) {
                            items(
                                items = state.value.favoriteMovies,
                                key = { it.id },
                                itemContent = { MovieCardContent(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
@Preview
private fun MoviesScreenPreview() {
    MoviesScreen(
        modifier = Modifier
    )
}