package com.alterjuice.task.moviedb.core.ui.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.alterjuice.task.moviedb.core.ui.BuildConfig
import com.alterjuice.utils.str.Str
import kotlinx.coroutines.flow.Flow



interface BaseSideEffect {
    data class ShowSnackbarEffect(val message: Str) : BaseSideEffect
}


interface EffectHandler<I: BaseSideEffect> {
    fun canHandle(effect: BaseSideEffect): Boolean
    suspend fun handle(effect: BaseSideEffect)


    companion object {
        inline fun <reified I: BaseSideEffect> ofType(
            crossinline block: suspend (effect: I) -> Unit
        ) = object : EffectHandler<I> {
            override fun canHandle(effect: BaseSideEffect): Boolean = effect is I
            override suspend fun handle(effect: BaseSideEffect) {
                if (effect is I) { block(effect) }
            }
        }
    }
}

@Composable
inline fun <reified T: BaseSideEffect> rememberEffectHandlerOfType(
    key1: Any? = true,
    crossinline block: suspend (effect: T) -> Unit
): EffectHandler<T> {
    return remember(key1) {
        EffectHandler.ofType<T>(block)
    }
}

@Composable
fun EffectsCollector(
    effects: Flow<BaseSideEffect>,
    vararg handlers: EffectHandler<*>
) {
    LaunchedEffect(effects, handlers) {
        effects.collect { effect ->
            for (handler in handlers) {
                if (handler.canHandle(effect)) {
                    handler.handle(effect)
                    return@collect
                }
            }
            if (BuildConfig.DEBUG) {
                throw IllegalArgumentException("No handler found for effect: $effect")
            } else {
                Log.w("EffectsCollector", "Unhandled effect: $effect")
            }
        }
    }
}