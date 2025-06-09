package com.alterjuice.task.moviedb.feature.movies.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate


@Immutable
data class MovieUI(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: MovieUIReleaseDate,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val isFavorite: Boolean = false
)

@Immutable
data class MovieUIReleaseDate(
    val year: Int,
    val month: Int,
    val day: Int
)

fun LocalDate.toMovieUIReleaseDate() = MovieUIReleaseDate(
    year = this.year,
    month = this.monthValue,
    day = this.dayOfMonth
)

fun MovieUIReleaseDate.toLocalDate() = LocalDate.of(year, month, day)