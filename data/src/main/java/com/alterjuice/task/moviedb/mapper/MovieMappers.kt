package com.alterjuice.task.moviedb.mapper

import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.network.model.MovieDto
import java.time.LocalDate

fun MovieDto.toDomain() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterPath,
    releaseDate = LocalDate.parse(releaseDate),
    voteAverage = voteAverage,
    voteCount = voteCount,
)