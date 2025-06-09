package com.alterjuice.task.moviedb.feature.movies.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter

@Composable
internal fun MovieAsyncImage(
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
private fun MovieAsyncImagePreview() {
    val context = LocalContext.current
    MovieAsyncImage(
        modifier = Modifier,
        moviePosterUrl = "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
        imageLoader = remember { ImageLoader.Builder(context).build() },
    )
}