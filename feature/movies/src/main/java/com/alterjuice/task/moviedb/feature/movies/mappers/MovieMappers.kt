package com.alterjuice.task.moviedb.feature.movies.mappers

import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.feature.movies.model.MovieUI
import com.alterjuice.task.moviedb.feature.movies.model.MovieUIRating
import com.alterjuice.task.moviedb.feature.movies.model.toMovieUIReleaseDate

fun Movie.toUI(): MovieUI {
    return MovieUI(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        releaseDate = releaseDate.toMovieUIReleaseDate(),
        rating = MovieUIRating(voteAverage),
        voteCount = voteCount,
        isFavorite = isFavorite,
    )
}