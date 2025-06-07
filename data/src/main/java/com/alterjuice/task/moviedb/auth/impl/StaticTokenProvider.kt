package com.alterjuice.task.moviedb.auth.impl

import com.alterjuice.task.moviedb.BuildConfig
import com.alterjuice.task.moviedb.auth.TokenProvider

internal class StaticAuthProvider: TokenProvider {
    override suspend fun getToken(): String? {
        return BuildConfig.TMDB_API_TOKEN
    }
}