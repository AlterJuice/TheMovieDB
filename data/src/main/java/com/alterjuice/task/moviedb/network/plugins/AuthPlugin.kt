package com.alterjuice.task.moviedb.network.plugins

import com.alterjuice.task.moviedb.auth.TokenProvider
import io.ktor.client.plugins.api.SendingRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
/**
 * Configuration class for the AuthPlugin in Ktor client.
 * Contains the [tokenProvider] which is required to supply authentication tokens.
 */
internal class AuthPluginConfig {
    /**
     * Provider responsible for supplying the authentication token.
     */
    var tokenProvider: TokenProvider? = null
}

/**
 * Ktor client plugin that automatically adds an Authorization header to requests.
 * Uses [TokenProvider] to retrieve the authentication token and applies it as a Bearer token
 * in the request's header if available. Requires the [tokenProvider] field in [AuthPluginConfig]
 * to be set; otherwise, an exception is thrown.
 */
internal val KtorAuthPlugin = createClientPlugin("AuthPlugin", ::AuthPluginConfig) {
    val tokenProvider = pluginConfig.tokenProvider ?: error("TokenProvider must be provided")

    on(SendingRequest) { request, content ->
        val token = tokenProvider.getToken()
        if (token != null) {
            request.header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}