package com.alterjuice.task.moviedb.database.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alterjuice.task.moviedb.database.AppDatabaseConsts

@Entity(tableName = AppDatabaseConsts.DATABASE_TABLE_MOVIES)
internal data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false
)