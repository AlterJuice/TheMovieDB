package com.alterjuice.task.moviedb.feature.movies.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alterjuice.task.moviedb.core.ui.components.MovieCard
import com.alterjuice.task.moviedb.core.ui.components.MovieUI
import com.alterjuice.task.moviedb.core.ui.components.testMovie

enum class MoviesScreen(
    val str: String,
) {
    All("All"),
    Favourites("Favourites"),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    modifier: Modifier
) {
    Scaffold(
        modifier = modifier,
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
        }
    ) {  paddings ->
        Surface(
            modifier = modifier.padding(paddings),
        ) {
            val tabs = remember { MoviesScreen.entries }
            val selected = remember { mutableIntStateOf(0) }
            Column {
                SecondaryTabRow(
                    selectedTabIndex = 0,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selected.intValue == index,
                            onClick = { selected.intValue = index },
                            text = {
                                Text(text = tab.str, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        )
                    }
                }
                MoviesContent(
                    modifier = Modifier,
                    items = (0..10).map { testMovie.copy(id = it)}
                )
            }
        }
    }
}

@Composable
fun AllMoviesContent() {

}


@Composable
fun FavouritesMoviesContent() {

}

@Composable
fun MoviesContent(
    modifier: Modifier,
    items: List<MovieUI>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            MovieCard(
                modifier = Modifier,
                movie = item,
                onAddToFavourite = {},
                onRemoveFromFavourite = {},
                onShare = {},
            )
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