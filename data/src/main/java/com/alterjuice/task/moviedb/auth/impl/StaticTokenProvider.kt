package com.alterjuice.task.moviedb.auth.impl

import com.alterjuice.task.moviedb.BuildConfig
import com.alterjuice.task.moviedb.auth.TokenProvider
import javax.inject.Inject

internal class StaticAuthProvider @Inject constructor(): TokenProvider {
    override suspend fun getToken(): String? {
        return BuildConfig.TMDB_API_TOKEN
    }
}