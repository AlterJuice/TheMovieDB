package com.alterjuice.task.moviedb.feature.movies.mappers

import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.feature.movies.model.MovieUI

fun Movie.toUI() = MovieUI(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount,
    isFavorite = isFavorite,
)