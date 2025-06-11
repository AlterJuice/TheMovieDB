package com.alterjuice.task.moviedb.errors

import com.alterjuice.task.moviedb.feature.movies.R
import com.alterjuice.utils.str.Str
import com.alterjuice.utils.str.StrRes

data object GlobalErrorMessagesProvider: AppErrorMessageProvider<GlobalError> {
    override fun canProvideMessage(e: AppError): Boolean {
        return e is GlobalError
    }

    override fun provideMessage(e: GlobalError): Str {
        return when (e) {
            is GlobalError.ConnectionError -> when(e.type) {
                GlobalError.ConnectionError.Issue.NO_INTERNET -> StrRes(R.string.global_error_no_internet_connection)
                GlobalError.ConnectionError.Issue.BAD_INTERNET -> StrRes(R.string.global_error_bad_internet_connection)
            }
            is GlobalError.Unknown -> when (e.message) {
                null -> StrRes(R.string.unknown_error)
                else -> StrRes(R.string.unknown_error_args, e.message)
            }
        }
    }
}