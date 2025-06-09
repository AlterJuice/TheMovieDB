@file:OptIn(ExperimentalMaterial3Api::class)

package com.alterjuice.task.moviedb.feature.movies.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.feature.movies.model.MovieUI
import java.time.LocalDate

val testMovieUi = MovieUI(
    id = 1,
    title = "Title",
    overview = buildString { repeat(30) { append("Overview ") } },
    posterUrl = "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
    releaseDate = LocalDate.now(),
    voteAverage = 5.0,
    voteCount = 100
)
val testMovie = Movie(
    id = 1,
    title = "Title",
    overview = buildString { repeat(30) { append("Overview ") } },
    posterUrl = "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
    releaseDate = LocalDate.now(),
    voteAverage = 5.0,
    voteCount = 100
)



@Composable
fun MovieCard(
    modifier: Modifier,
    contentPaddings: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    movie: MovieUI,
    onAddToFavourite: () -> Unit,
    onRemoveFromFavourite: () -> Unit,
    onShare: () -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier.wrapContentSize().animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ),
        shape = CardDefaults.shape,
        colors = CardDefaults.cardColors(),
        onClick = { isExpanded = !isExpanded }
    ) {
        val context = LocalContext.current
        MovieAsyncImage(
            modifier = Modifier.fillMaxWidth().heightIn(max = 240.dp),
            moviePosterUrl = movie.posterUrl,
            imageLoader = remember {
                ImageLoader.Builder(context)
                    .crossfade(true)
                    .build()
            },
        )
        Column(
            modifier = Modifier.padding(contentPaddings)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier,
                    text = movie.title,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier,
                    text = movie.voteAverage.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Text(
                modifier = Modifier,
                text = movie.overview,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (movie.isFavorite) {
                    true -> MovieActionButton(text = "Remove", action = onRemoveFromFavourite)
                    false -> MovieActionButton(text = "Like", action = onAddToFavourite)
                }
                MovieActionButton(text = "Share", action = onShare)
            }
        }
    }
}

@Composable
fun MovieActionButton(
    modifier: Modifier = Modifier,
    text: String,
    action: () -> Unit
) {
    TextButton(
        onClick = action,
        modifier = modifier,
    ) {
        Text(text)
    }
}


@Composable
fun MovieAsyncImage(
    modifier: Modifier,
    moviePosterUrl: String?,
    imageLoader: ImageLoader,
    contentDescription: String? = null
) {
    val painter = rememberAsyncImagePainter(
        model = moviePosterUrl,
        contentScale = ContentScale.FillWidth,
        imageLoader = imageLoader
    )
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = contentDescription
    )
}

@Composable
@Preview
private fun MovieCardPreview() {
    val movie = remember { mutableStateOf(
        testMovieUi
    )}
    MovieCard(
        modifier = Modifier,
        movie = movie.value,
        onShare = {},
        onAddToFavourite = { movie.value = movie.value.copy(isFavorite = true) },
        onRemoveFromFavourite = { movie.value = movie.value.copy(isFavorite = false) },
    )
}