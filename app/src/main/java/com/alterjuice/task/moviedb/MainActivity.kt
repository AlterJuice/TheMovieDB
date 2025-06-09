package com.alterjuice.task.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import coil.ImageLoader
import com.alterjuice.task.moviedb.core.ui.theme.TheMovieDBTheme
import com.alterjuice.task.moviedb.feature.movies.ui.screens.MoviesScreen
import com.alterjuice.task.moviedb.feature.movies.ui.utils.LocalImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMovieDBTheme {
                CompositionLocalProvider(
                    LocalImageLoader provides imageLoader
                ) {
                    MoviesScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
