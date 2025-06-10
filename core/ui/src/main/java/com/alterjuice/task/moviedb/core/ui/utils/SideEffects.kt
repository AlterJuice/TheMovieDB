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
 * @param effects The [Flow] of [BaseSideEffect]s to collect and process.
 * @param handlers A vararg list of [EffectHandler]s that will be checked in order to handle each effect.
 *                 The first handler that returns `true` from [EffectHandler.canHandle] will process the effect.
 * @param strategy The strategy to use when no handler is found for an effect.
 *                 Defaults to [UnhandledEffectStrategyThrowException], which throws an [IllegalStateException].
 *                 Use [UnhandledEffectStrategyLogging] to log a warning or [UnhandledEffectStrategyIgnore] to silently drop unhandled effects.
 */
@Composable
fun EffectsCollector(
    effects: Flow<BaseSideEffect>,
    vararg handlers: EffectHandler<*>,
    strategy: UnhandledEffectsStrategy = UnhandledEffectStrategyThrowException
) {
    LaunchedEffect(effects, handlers) {
        effects.collect { effect ->
            launch {
                // Handle each effect in a separate job to:
                // - Avoid blocking the coroutine when using `.collect { }`
                // - Avoid cancellation issues when using `.collectLatest { }`
                for (handler in handlers) {
                    if (handler.canHandle(effect)) {
                        handler.handle(effect)
                        return@launch
                    }
                }
                strategy.onUnhandled(effect)
            }
        }
    }
}
/**
 * A functional interface defining the strategy to handle unprocessed [BaseSideEffect]s in [EffectsCollector].
 *
 * Implementations define how unhandled effects should be treated, such as throwing exceptions,
 * logging warnings, or ignoring them entirely.
 */
fun interface UnhandledEffectsStrategy {
    /**
     * Executes the strategy for the specified unhandled [BaseSideEffect].
     *
     * @param effect The unprocessed side effect to handle.
     */
    fun onUnhandled(effect: BaseSideEffect): Unit
}

/**
 * Strategy that throws an [IllegalStateException] when an [BaseSideEffect] is unhandled.
 *
 * This is the default behavior and ensures early detection of missing effect handlers
 * during development by throwing an exception.
 */
data object UnhandledEffectStrategyThrowException: UnhandledEffectsStrategy {
    override fun onUnhandled(effect: BaseSideEffect) {
        throw IllegalStateException("No handler found for effect: $effect")
    }
}

/**
 * Strategy that logs a warning when an [BaseSideEffect] is unhandled.
 *
 * This is useful in production environments to log missing effect handlers without interrupting
 * execution. Uses [android.util.Log.w] to log to "EffectsCollector".
 */
data object UnhandledEffectStrategyLogging: UnhandledEffectsStrategy {
    override fun onUnhandled(effect: BaseSideEffect) {
        Log.w("EffectsCollector", "Unhandled effect: $effect")
    }
}

/**
 * Strategy that silently ignores unhandled [BaseSideEffect]s.
 *
 * This is useful when effects may be optional or when missing handlers are expected behavior.
 * No warnings or exceptions will be generated.
 */
data object UnhandledEffectStrategyIgnore: UnhandledEffectsStrategy {
    override fun onUnhandled(effect: BaseSideEffect) {
        // Nothing to do
    }
}