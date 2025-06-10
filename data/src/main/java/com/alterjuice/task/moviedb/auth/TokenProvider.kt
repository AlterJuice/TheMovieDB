package com.alterjuice.task.moviedb.auth

/**
 * Functional interface for providing authentication tokens.
 * Used to supply tokens for authorizing network requests, typically in conjunction with [KtorAuthPlugin].
 */
fun interface TokenProvider {
    /**
     * Retrieves the authentication token asynchronously.
     *
     * This is a [suspend] function as it may need to perform asynchronous operations (e.g., fetching from secure storage).
     * Returns [String] when a valid token is available, or [null] if no token is currently available.
     */
    suspend fun getToken(): String?
}