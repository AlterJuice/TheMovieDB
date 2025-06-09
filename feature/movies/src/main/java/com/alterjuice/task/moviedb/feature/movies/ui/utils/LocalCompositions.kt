package com.alterjuice.task.moviedb.feature.movies.ui.utils

import androidx.compose.runtime.staticCompositionLocalOf
import coil.ImageLoader

val LocalImageLoader = staticCompositionLocalOf<ImageLoader> {
    error("No provider for LocalImageLoader")
}