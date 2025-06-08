package com.alterjuice.task.moviedb.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alterjuice.task.moviedb.database.AppDatabase
import com.alterjuice.task.moviedb.domain.model.Movie
import com.alterjuice.task.moviedb.domain.repository.MovieRepository
import com.alterjuice.task.moviedb.mapper.toDomain
import com.alterjuice.task.moviedb.network.service.MovieApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val database: AppDatabase
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MoviesRemoteMediator(apiService, database),
            pagingSourceFactory = { database.movieDao().pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { movieEntity -> movieEntity.toDomain() }
        }
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return database.movieDao().getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun removeFromFavourites(movieId: Int) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite = false)
    }

    override suspend fun addToFavourites(movieId: Int) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite = true)
    }

}
