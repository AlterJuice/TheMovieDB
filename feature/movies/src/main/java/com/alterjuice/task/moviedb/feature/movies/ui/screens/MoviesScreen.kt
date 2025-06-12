@file:OptIn(ExperimentalContracts::class, ExperimentalMaterial3Api::class)

package com.alterjuice.task.moviedb.feature.movies.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.alterjuice.task.moviedb.core.ui.BuildConfig
import com.alterjuice.task.moviedb.core.ui.components.BaseScreen
import com.alterjuice.task.moviedb.core.ui.components.HorizontallyAnimatedContent
import com.alterjuice.task.moviedb.core.ui.components.Loader
import com.alterjuice.task.moviedb.core.ui.components.rememberLoaderOptions
import com.alterjuice.task.moviedb.core.ui.components.rememberSnackbarEffectHandler
import com.alterjuice.task.moviedb.core.ui.extensions.get
import com.alterjuice.task.moviedb.core.ui.extensions.pagedItems
import com.alterjuice.task.moviedb.core.ui.utils.EffectsCollector
import com.alterjuice.task.moviedb.core.ui.utils.UnhandledEffectStrategyLogging
import com.alterjuice.task.moviedb.core.ui.utils.UnhandledEffectStrategyThrowException
import com.alterjuice.task.moviedb.feature.movies.R
import com.alterjuice.task.moviedb.feature.movies.model.MovieListItem
import com.alterjuice.task.moviedb.feature.movies.model.MoviesEvent
import com.alterjuice.task.moviedb.feature.movies.model.MoviesTab
import com.alterjuice.task.moviedb.feature.movies.model.lazyListKey
import com.alterjuice.task.moviedb.feature.movies.ui.components.MovieCard
import com.alterjuice.task.moviedb.feature.movies.ui.components.MovieTabsComponent
import com.alterjuice.task.moviedb.feature.movies.ui.components.MoviesLazyList
import com.alterjuice.task.moviedb.feature.movies.ui.utils.rememberShareEffectHandler
import com.alterjuice.task.moviedb.feature.movies.viewmodel.MoviesViewModel
import com.alterjuice.utils.str.Str
import kotlinx.collections.immutable.toPersistentList
import kotlin.contracts.ExperimentalContracts


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier,
    vm: MoviesViewModel = hiltViewModel<MoviesViewModel>(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessageEffectHandler = rememberSnackbarEffectHandler(snackbarHostState)
    val shareMovieEffectHandler = rememberShareEffectHandler()

    @Composable
    fun LazyItemScope.MovieListItemContent(listItem: MovieListItem) {
        // Hardcoded Modifiers since it is inner composable function
        when (listItem) {
            is MovieListItem.Movie -> {
                MovieCard(
                    modifier = Modifier.fillMaxWidth().animateItem(),
                    movie = listItem.movie,
                    onAddToFavourite = { vm.onEvent(MoviesEvent.AddToFavorites(listItem.movie.id)) },
                    onRemoveFromFavourite = { vm.onEvent(MoviesEvent.RemoveFromFavorites(listItem.movie.id)) },
                    onShare = { vm.onEvent(MoviesEvent.ShareMovie(listItem.movie.id, listItem.movie.title)) },
                )
            }

            is MovieListItem.Separator -> {
                Row(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = listItem.monthAndYear,
                        modifier = Modifier,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }

    EffectsCollector(
        effects = vm.effect,
        snackbarMessageEffectHandler,
        shareMovieEffectHandler,
        strategy = if (BuildConfig.DEBUG) UnhandledEffectStrategyThrowException else UnhandledEffectStrategyLogging
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
        val tabs = remember { MoviesTab.entries.toPersistentList() }
        val state = vm.state.collectAsStateWithLifecycle()
        val movies = state.value.movies.collectAsLazyPagingItems()

        Column(
            modifier = Modifier.padding(paddings)
        ) {
            MovieTabsComponent(
                modifier = Modifier.fillMaxWidth(),
                tabs = tabs,
                selectedTab = state.value.selectedTab,
                onTabSelected = { vm.onEvent(MoviesEvent.SelectTab(it)) }
            )


            val allMoviesState = rememberLazyListState()
            val favoriteMoviesState = rememberLazyListState()
            HorizontallyAnimatedContent(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
                targetState = state.value.selectedTab,
                stateToOrderIndex = { tabs.indexOf(it) }
            ) { target ->
                when (target) {
                    MoviesTab.ALL -> {
                        AllMoviesTabContent(
                            modifier = Modifier.fillMaxSize(),
                            state = allMoviesState,
                            onRefresh = { vm.onEvent(MoviesEvent.Refresh) },
                            pagingError = state.value.pagingError,
                            onError = vm::onEvent,
                            items = movies,
                            movieCardItemContent = { MovieListItemContent(it) }
                        )
                    }

                    MoviesTab.FAVORITES -> {
                        MoviesLazyList(
                            modifier = Modifier,
                            state = favoriteMoviesState
                        ) {
                            items(
                                items = state.value.favoriteMovies,
                                key = { item -> item.lazyListKey() },
                                itemContent = { listItem ->
                                    MovieListItemContent(listItem)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllMoviesTabContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<MovieListItem>,
    state: LazyListState,
    pagingError: Str?,
    onRefresh: () -> Unit,
    onError: (MoviesEvent.PagingError) -> Unit,
    movieCardItemContent: @Composable LazyItemScope.(MovieListItem) -> Unit,
) {
    val isRefreshing = remember {
        derivedStateOf {
            items.loadState.refresh is LoadState.Loading
        }
    }

    LaunchedEffect(key1 = items.loadState.refresh) {
        val refreshState = items.loadState.refresh
        if (refreshState is LoadState.Error) {
            onError(MoviesEvent.PagingError(refreshState.error))
        }
    }

    val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(items.loadState.refresh) {
        // Requests scroll to first element
        if (items.loadState.refresh is LoadState.NotLoading && items.itemCount > 0) {
            state.animateScrollToItem(0)
        }
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing.value,
        state = pullRefreshState,
        onRefresh = onRefresh,
    ) {
        val shouldShowLoader = remember {
            derivedStateOf {
                items.loadState.refresh is LoadState.Loading && items.itemCount == 0
            }
        }

        if (pagingError != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = pagingError.get())
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRefresh) {
                        Text(text = stringResource(R.string.try_refresh_movies_again))
                    }
                }
            }
        } else if (shouldShowLoader.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Loader(
                    modifier = Modifier,
                    options = rememberLoaderOptions(
                        size = 60.dp
                    )
                )
            }
        } else {
            MoviesLazyList(
                modifier = Modifier.fillMaxSize(),
                state = state
            ) {
                pagedItems(
                    items = items,
                    key = { it.lazyListKey() },
                    contentType = { when(it) {
                        is MovieListItem.Movie -> "Movie"
                        is MovieListItem.Separator -> "Separator"
                    }},
                    itemContent = { movieCardItemContent(it) }
                )


                item {
                    when (items.loadState.append) {
                        is LoadState.Loading -> Loader(
                            modifier = Modifier,
                            options = rememberLoaderOptions(
                                size = 60.dp,
                                padding = PaddingValues(8.dp),
                            )
                        )

                        is LoadState.Error -> Button(
                            modifier = Modifier,
                            onClick = { items.retry() }
                        ) {
                            Text(text = stringResource(R.string.try_load_more_movies_again))
                        }

                        is LoadState.NotLoading -> Unit
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