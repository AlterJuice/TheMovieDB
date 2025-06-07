package com.alterjuice.task.moviedb.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponseDto(
    @SerialName("page") val page: Int = 0,
    @SerialName("results") val results: List<MovieDto> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0
)