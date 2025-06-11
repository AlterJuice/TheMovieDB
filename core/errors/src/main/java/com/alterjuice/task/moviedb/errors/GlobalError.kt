package com.alterjuice.task.moviedb.errors

sealed class GlobalError(message: String? = null, cause: Throwable? = null) : AppError(message, cause){

    class ConnectionError(val type: Issue): GlobalError("Connection issue: $type") {
        enum class Issue {
            NO_INTERNET,
            BAD_INTERNET;
            operator fun invoke() = ConnectionError(this)
        }
    }
    class Unknown(t: Throwable? = null) : GlobalError("Unknown", cause = t)
}