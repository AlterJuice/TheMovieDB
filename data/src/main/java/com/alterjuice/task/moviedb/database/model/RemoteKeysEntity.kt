package com.alterjuice.task.moviedb.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alterjuice.task.moviedb.database.AppDatabaseConsts

@Entity(tableName = AppDatabaseConsts.DATABASE_TABLE_REMOTE_KEYS)
internal data class RemoteKeysEntity(
    @PrimaryKey val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)