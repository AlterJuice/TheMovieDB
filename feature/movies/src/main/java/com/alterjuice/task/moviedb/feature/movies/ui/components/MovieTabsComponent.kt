package com.alterjuice.task.moviedb.feature.movies.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.alterjuice.task.moviedb.core.ui.extensions.get
import com.alterjuice.task.moviedb.feature.movies.model.MoviesTab
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MovieTabsComponent(
    modifier: Modifier = Modifier,
    tabs: List<MoviesTab>,
    selectedTab: MoviesTab,
    onTabSelected: (MoviesTab) -> Unit = {},
) {
    SecondaryTabRow(
        modifier = modifier,
        selectedTabIndex = tabs.indexOf(selectedTab),
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = selectedTab.ordinal == tab.ordinal,
                onClick = { onTabSelected(tab)},
                text = {
                    Text(
                        text = tab.str.get(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun MovieTabsComponentPreview() {
    MovieTabsComponent(
        modifier = Modifier.fillMaxWidth(),
        tabs = remember { MoviesTab.entries },
        selectedTab = MoviesTab.FAVORITES,
        onTabSelected = { },
    )
}