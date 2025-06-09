package com.alterjuice.task.moviedb.core.ui.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.alterjuice.task.moviedb.core.ui.BuildConfig
import com.alterjuice.utils.str.Str
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * Marker interface for all side effects in MVI architecture.
 *
 * Side effects represent one-time UI actions that are not part of the state,
 * such as navigation, toasts, or snackbars.
 */
interface BaseSideEffect {

    /**
     * A concrete implementation of [BaseSideEffect] used to display a snackbar.
     *
     * @param message The message to be shown.
     */
    data class ShowSnackbarEffect(val message: Str) : BaseSideEffect
}


/**
 * Defines a handler for a specific type of [BaseSideEffect].
 *
 * @param I The specific type of side effect this handler can process.
 */
interface EffectHandler<E: BaseSideEffect> {
    /**
     * Checks whether this handler can handle the given effect.
     *
     * @param effect The side effect to check.
     * @return true if this handler supports the given effect, false otherwise.
     */
    fun canHandle(effect: BaseSideEffect): Boolean

    /**
     * Handles the given effect.
     *
     * @param effect The effect to be handled.
     */
    suspend fun handle(effect: BaseSideEffect)


    companion object {
        /**
         * Creates an [EffectHandler] for a specific subtype of [BaseSideEffect].
         *
         * @param block A suspend lambda to handle the effect.
         * @return An [EffectHandler] instance that handles effects of type [I].
         */
        inline fun <reified E: BaseSideEffect> ofType(
            crossinline block: suspend (effect: E) -> Unit
        ) = object : EffectHandler<E> {
            override fun canHandle(effect: BaseSideEffect): Boolean = effect is E
            override suspend fun handle(effect: BaseSideEffect) {
                if (effect is E) { block(effect) }
            }
        }
    }
}


/**
 * Remembers a composable-aware [EffectHandler] for a specific effect type [T].
 *
 * @param block A suspend lambda to handle the effect.
 * @return A remembered [EffectHandler] instance.
 */
@Composable
inline fun <reified T: BaseSideEffect> rememberEffectHandlerOfType(
    key1: Any? = true,
    crossinline block: suspend (effect: T) -> Unit
): EffectHandler<T> {
    return remember(key1) {
        EffectHandler.ofType<T>(block)
    }
}

/**
 * Collects side effects from a [Flow] and delegates them to the appropriate [EffectHandler].
 *
 * @param effects A [Flow] of [BaseSideEffect]s to collect.
 * @param handlers A vararg list of [EffectHandler]s used to handle incoming effects.
 *
 * Only the first handler that returns true from [EffectHandler.canHandle] will process the effect.
 * If no handler is found, an exception is thrown in DEBUG builds or a warning is logged otherwise.
 */
@Composable
fun EffectsCollector(
    effects: Flow<BaseSideEffect>,
    vararg handlers: EffectHandler<*>
) {
    LaunchedEffect(effects, handlers) {
        effects.collect { effect ->
            launch {
                // Handle each effect in separate job
                // to avoid blocking the coroutine in case of using .collect { }
                // and to avoid cancellation in case of using .collectLatest { }
                for (handler in handlers) {
                    if (handler.canHandle(effect)) {
                        handler.handle(effect)
                        return@launch
                    }
                }
                if (BuildConfig.DEBUG) {
                    throw IllegalStateException("No handler found for effect: $effect")
                } else {
                    Log.w("EffectsCollector", "Unhandled effect: $effect")
                }
            }
        }
    }
}