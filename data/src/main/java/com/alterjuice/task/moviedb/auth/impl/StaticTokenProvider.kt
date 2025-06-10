package com.alterjuice.task.moviedb.auth.impl

import com.alterjuice.task.moviedb.BuildConfig
import com.alterjuice.task.moviedb.auth.TokenProvider
import javax.inject.Inject

/**
 * A [TokenProvider] implementation that provides a static authentication token
 * defined in the build configuration. This is typically used for scenarios with
 * fixed credentials, such as API key authentication for services like TMDB.
 * 
 * @see BuildConfig.TMDB_API_TOKEN
 */
internal class StaticAuthProvider @Inject constructor(): TokenProvider {
    /**
     * Returns the static authentication token from [BuildConfig.TMDB_API_TOKEN].
     * 
     * This implementation does not perform asynchronous operations since the token
     * is available at compile time through the build configuration. It follows the
     * [TokenProvider] interface contract by marking it [suspend], but no suspension
     * is actually required here.
     */
    override suspend fun getToken(): String? {
        return BuildConfig.TMDB_API_TOKEN
    }
}