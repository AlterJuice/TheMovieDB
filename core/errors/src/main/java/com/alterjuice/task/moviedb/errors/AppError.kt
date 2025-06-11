@file:OptIn(ExperimentalContracts::class)

package com.alterjuice.task.moviedb.errors

import com.alterjuice.utils.str.Str
import kotlin.contracts.ExperimentalContracts

/**
 * Basic Error type for App
 * */
open class AppError(
    message: String? = null,
    cause: Throwable? = null,
) : Throwable(message, cause)


/**
 * Basic message provider for Error (AppError) class
 * @see AppError
 * */
interface AppErrorMessageProvider<T : AppError> {
    fun canProvideMessage(e: AppError): Boolean
    fun provideMessage(e: T): Str
}