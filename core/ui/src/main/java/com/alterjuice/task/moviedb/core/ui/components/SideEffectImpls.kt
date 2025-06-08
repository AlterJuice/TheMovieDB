package com.alterjuice.task.moviedb.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alterjuice.task.moviedb.core.ui.utils.BaseSideEffect.ShowSnackbarEffect
import com.alterjuice.task.moviedb.core.ui.utils.EffectHandler
import com.alterjuice.task.moviedb.core.ui.utils.rememberEffectHandlerOfType
import com.alterjuice.utils.str.get

@Composable
fun rememberSnackbarEffectHandler(
    snackbarHostState: SnackbarHostState
): EffectHandler<ShowSnackbarEffect> {
    val context = LocalContext.current
    return rememberEffectHandlerOfType(snackbarHostState) { effect ->
        snackbarHostState.showSnackbar(effect.message.get(context))
    }
}