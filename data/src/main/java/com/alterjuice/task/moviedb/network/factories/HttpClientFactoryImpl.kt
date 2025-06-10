package com.alterjuice.task.moviedb.network.factories

import android.util.Log
import com.alterjuice.task.moviedb.BuildConfig
import com.alterjuice.task.moviedb.auth.TokenProvider
import com.alterjuice.task.moviedb.network.plugins.KtorAuthPlugin
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Implementation of [HttpClientFactory] that creates and configures a Ktor [HttpClient] with necessary
 * plugins for logging, JSON serialization, default request base URL, and authentication.
 *
 * @property tokenProvider Supplies authentication tokens for request headers
 */
internal class HttpClientFactoryImpl @Inject constructor(
    private val tokenProvider: TokenProvider
): HttpClientFactory {

    /**
     * Creates a configured [HttpClient] using the CIO engine with logging, content negotiation,
     * default request settings, and authentication plugin installed.
     */
    override fun create(): HttpClient {
        return HttpClient(CIO) {
            installLogging()
            installJsonNegotiation()
            installDefaultRequests()
            installAuthentication()
        }
    }

    private fun HttpClientConfig<*>.installLogging() {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KTOR", message)
                }
            }
            level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
        }
    }

    private fun HttpClientConfig<*>.installJsonNegotiation() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private fun HttpClientConfig<*>.installDefaultRequests() {
        install(DefaultRequest) {
            url(BuildConfig.TMDB_BASE_URL)
        }
    }

    private fun HttpClientConfig<*>.installAuthentication() {
        install(KtorAuthPlugin) {
            this.tokenProvider = this@HttpClientFactoryImpl.tokenProvider
        }
    }
}