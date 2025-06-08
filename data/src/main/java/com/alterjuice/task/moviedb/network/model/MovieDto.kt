package com.alterjuice.task.moviedb.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 *
 * @see <a href="https://developer.themoviedb.org/reference/discover-movie">Discover Movie API</a>
 * */
@Serializable
internal data class MovieDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("adult") val adult: Boolean = true,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("original_language") val originalLanguage: String = "",
    @SerialName("original_title") val originalTitle: String = "",
    @SerialName("overview") val overview: String = "",
    @SerialName("popularity") val popularity: Double = 0.0,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("video") val isVideo: Boolean = true,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0
)