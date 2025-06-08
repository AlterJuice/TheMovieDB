package com.alterjuice.task.moviedb.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alterjuice.task.moviedb.database.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY releaseDate DESC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies WHERE isFavorite = 0")
    suspend fun clearAllButFavorites()

    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY releaseDate DESC")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavoriteStatus(movieId: Int, isFavorite: Boolean)
}