package com.alterjuice.task.moviedb.mapper

import com.alterjuice.task.moviedb.database.model.MovieEntity
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.network.model.MovieDto
import java.time.LocalDate
import java.time.format.DateTimeParseException

internal fun MovieDto.toEntity(
    posterBaseUrl: String
): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterPath?.let { posterBaseUrl + it },
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}

internal fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        releaseDate = try { LocalDate.parse(this.releaseDate) } catch (e: DateTimeParseException) { LocalDate.MIN },
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        isFavorite = this.isFavorite
    )
}