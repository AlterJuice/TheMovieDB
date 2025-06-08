package com.alterjuice.task.moviedb.network.factories

import io.ktor.client.HttpClient

interface HttpClientFactory {
    fun create(): HttpClient
}
