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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
                initialLoadSize = 20,
                enablePlaceholders = true
            ),
            remoteMediator = MoviesRemoteMediator(apiService, database),
            pagingSourceFactory = { database.movieDao().pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { movieEntity -> movieEntity.toDomain() }
        }
    }

    override fun getFavoriteMovies(): Flow<ImmutableList<Movie>> {
        return database.movieDao().getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }.toImmutableList()
        }
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite = false)
    }

    override suspend fun addToFavorites(movieId: Int) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite = true)
    }

}
