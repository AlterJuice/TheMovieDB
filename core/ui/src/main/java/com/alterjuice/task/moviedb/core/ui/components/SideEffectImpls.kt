package com.alterjuice.task.moviedb.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect.ShowSnackbarEffect
import com.alterjuice.task.moviedb.core.ui.utils.EffectHandler
import com.alterjuice.task.moviedb.core.ui.utils.rememberEffectHandlerOfType
import com.alterjuice.utils.str.get


/**
 * Provides an [EffectHandler] that shows a snackbar using the provided [SnackbarHostState].
 *
 * This is a specialized handler for [ShowSnackbarEffect].
 *
 * @param snackbarHostState The [SnackbarHostState] used to show the snackbar.
 * @return A remembered [EffectHandler] that handles [ShowSnackbarEffect].
 */
@Composable
fun rememberSnackbarEffectHandler(
    snackbarHostState: SnackbarHostState
): EffectHandler<ShowSnackbarEffect> {
    val context = LocalContext.current
    return rememberEffectHandlerOfType(snackbarHostState) { effect ->
        snackbarHostState.showSnackbar(effect.message.get(context))
    }
}