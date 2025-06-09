package com.alterjuice.task.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import com.alterjuice.task.moviedb.core.ui.theme.TheMovieDBTheme
import com.alterjuice.task.moviedb.feature.movies.ui.screens.MoviesScreen
import com.alterjuice.task.moviedb.feature.movies.ui.utils.LocalImageLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMovieDBTheme {
                val context = LocalContext.current
                val imageLoader = remember {
                    ImageLoader.Builder(context).crossfade(true).build()
                }
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheMovieDBTheme {
        Greeting("Android")
    }
}