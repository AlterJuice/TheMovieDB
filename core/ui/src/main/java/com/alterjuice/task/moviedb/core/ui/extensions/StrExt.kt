package com.alterjuice.task.moviedb.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.alterjuice.utils.str.Str
import com.alterjuice.utils.str.get

/**
 * Composable extension function to retrieve a localized [String] representation of this [Str].
 *
 * This function uses [LocalContext.current] to resolve the string resource and [LocalConfiguration.current]
 * to trigger recomposition when the system configuration (e.g. locale) changes.
 *
 * @return The resolved string value
 */
@Composable
fun Str.get(): String {
    LocalConfiguration.current
    return this.get(LocalContext.current)
}