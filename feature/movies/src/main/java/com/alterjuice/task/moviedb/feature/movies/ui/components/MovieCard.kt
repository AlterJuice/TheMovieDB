@file:OptIn(ExperimentalMaterial3Api::class)

package com.alterjuice.task.moviedb.feature.movies.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alterjuice.task.moviedb.feature.movies.model.MovieUI
import com.alterjuice.task.moviedb.feature.movies.model.toMovieUIReleaseDate
import com.alterjuice.task.moviedb.feature.movies.ui.utils.LocalImageLoader
import java.time.LocalDate

private const val MOVIE_CARD_OVERVIEW_MAX_LINES_COUNT = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: MovieUI,
    onAddToFavourite: () -> Unit,
    onRemoveFromFavourite: () -> Unit,
    onShare: () -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val posterHeight = 240.dp
    Card(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        onClick = { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            MovieAsyncImage(
                modifier = Modifier
                    .height(posterHeight)
                    .aspectRatio(2f / 3f),
                moviePosterUrl = movie.posterUrl,
                imageLoader = LocalImageLoader.current
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.voteAverage.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else MOVIE_CARD_OVERVIEW_MAX_LINES_COUNT,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    AnimatedFavoriteIcon(
                        modifier = Modifier,
                        isFavorite = movie.isFavorite,
                    ) {
                        if (movie.isFavorite) onRemoveFromFavourite()
                        else onAddToFavourite()
                    }
                    ShareButton(modifier = Modifier, onShare = onShare)
                }
            }
        }
    }
}


@Composable
@Preview
private fun MovieCardPreview() {
    val movie = remember { mutableStateOf(
        MovieUI(
            id = 1,
            title = "Title",
            overview = buildString { repeat(30) { append("Overview ") } },
            posterUrl = "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
            releaseDate = LocalDate.now().toMovieUIReleaseDate(),
            voteAverage = 5.0,
            voteCount = 100
        )
    )}
    MovieCard(
        modifier = Modifier,
        movie = movie.value,
        onShare = {},
        onAddToFavourite = { movie.value = movie.value.copy(isFavorite = true) },
        onRemoveFromFavourite = { movie.value = movie.value.copy(isFavorite = false) },
    )
}