package com.alterjuice.task.moviedb.feature.movies.model

import com.alterjuice.utils.str.Str
import com.alterjuice.utils.str.StrRaw

enum class MoviesTab(
    val str: Str,
) {
    ALL(StrRaw("All")),
    FAVORITES(StrRaw("Favorites"));
}