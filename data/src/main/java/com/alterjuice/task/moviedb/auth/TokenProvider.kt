package com.alterjuice.task.moviedb.auth

fun interface TokenProvider {
    suspend fun getToken(): String?
}