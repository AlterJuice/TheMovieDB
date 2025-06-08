package com.alterjuice.task.moviedb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alterjuice.task.moviedb.database.dao.MovieDao
import com.alterjuice.task.moviedb.database.dao.RemoteKeysDao
import com.alterjuice.task.moviedb.database.model.MovieEntity
import com.alterjuice.task.moviedb.database.model.RemoteKeysEntity

@Database(
    entities = [MovieEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}