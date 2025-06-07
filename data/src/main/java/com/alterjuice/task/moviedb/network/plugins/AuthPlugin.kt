package com.alterjuice.task.moviedb.network.plugins

import com.alterjuice.task.moviedb.auth.TokenProvider
import io.ktor.client.plugins.api.SendingRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

internal class AuthPluginConfig {
    var tokenProvider: TokenProvider? = null
}


internal val KtorAuthPlugin = createClientPlugin("AuthPlugin", ::AuthPluginConfig) {
    val tokenProvider = pluginConfig.tokenProvider ?: error("TokenProvider must be provided")

    on(SendingRequest) { request, content ->
        val token = tokenProvider.getToken()
        if (token != null) {
            request.header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}