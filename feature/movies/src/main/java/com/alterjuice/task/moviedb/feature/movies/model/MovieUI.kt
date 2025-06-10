package com.alterjuice.task.moviedb.feature.movies.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.util.Locale


@Immutable
data class MovieUI(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: MovieUIReleaseDate,
    val rating: MovieUIRating,
    val voteCount: Int = 0,
    val isFavorite: Boolean = false
)

@Immutable
data class MovieUIReleaseDate(
    val year: Int,
    val month: Int,
    val day: Int
)


@JvmInline
value class MovieUIRating(val value: String) {
    constructor(rateAvg: Double) : this(format(rateAvg))

    companion object {
        private fun format(rateAvg: Double): String {
            return String.format(Locale.US, "★ %.1f", rateAvg)
        }
    }
}

fun LocalDate.toMovieUIReleaseDate() = MovieUIReleaseDate(
    year = this.year,
    month = this.monthValue,
    day = this.dayOfMonth
)

fun MovieUIReleaseDate.toLocalDate() = LocalDate.of(year, month, day)