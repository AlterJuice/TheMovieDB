package com.alterjuice.task.moviedb.network.factories

import io.ktor.client.HttpClient

/**
 * Factory interface for creating and configuring a Ktor [HttpClient] with necessary
 * plugins for logging, JSON serialization, default request settings, and authentication.
 */
interface HttpClientFactory {
    /**
     * Returns a configured [HttpClient] using the CIO engine with required plugins installed.
     */
    fun create(): HttpClient
}
