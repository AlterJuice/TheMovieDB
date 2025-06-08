package com.alterjuice.task.moviedb.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.alterjuice.utils.str.Str
import com.alterjuice.utils.str.get

@Composable
fun Str.get(): String {
    LocalConfiguration.current
    return this.get(LocalContext.current)
}