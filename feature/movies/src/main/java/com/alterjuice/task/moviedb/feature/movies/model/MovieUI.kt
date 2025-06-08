package com.alterjuice.task.moviedb.feature.movies.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate


@Immutable
data class MovieUI(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: LocalDate,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val isFavorite: Boolean = false
)