package com.alterjuice.task.moviedb.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.alterjuice.task.moviedb.database.AppDatabase
import com.alterjuice.task.moviedb.database.model.MovieEntity
import com.alterjuice.task.moviedb.database.model.RemoteKeysEntity
import com.alterjuice.task.moviedb.mapper.toEntity
import com.alterjuice.task.moviedb.network.service.MovieApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
internal class MoviesRemoteMediator(
    private val apiService: MovieApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, MovieEntity>() {

    private val movieDao = database.movieDao()
    private val remoteKeysDao = database.remoteKeysDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            Log.d("RemoteMediator", "Load call with type: $loadType")
            val currentPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }
            Log.d("RemoteMediator", "Making request for page: $currentPage")
            val response = withContext(Dispatchers.IO) {
                apiService.getMovies(page = currentPage)
            }
            Log.d("RemoteMediator", "Response: page=${response.page}, maxPage=${response.totalPages}, totalResults=${response.totalResults}")
            Log.d("RemoteMediator", "Response items: ${response.results.joinToString(", ", "[", "]") { it.releaseDate + " " + it.title }}")
            val moviesDto = response.results
            val endOfPaginationReached = response.page >= response.totalPages

            val prevKey = if (currentPage == 1) null else currentPage - 1
            val nextKey = if (endOfPaginationReached) null else currentPage + 1

            Log.d("RemoteMediator", "PrevKey: $prevKey, NextKey: $nextKey, CurrentPage: $currentPage")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAllButFavorites()
                    remoteKeysDao.clearRemoteKeys()
                }

                val movieEntities = moviesDto.map { it.toEntity(posterBaseUrl = POSTER_BASE_URL) }
                val keys = movieEntities.map { RemoteKeysEntity(movieId = it.id, prevKey = prevKey, nextKey = nextKey) }
                
                movieDao.upsertAll(movieEntities)
                remoteKeysDao.insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e("RemoteMediator", "Load failed with error", e)
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie -> remoteKeysDao.getRemoteKeyByMovieId(movie.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie -> remoteKeysDao.getRemoteKeyByMovieId(movie.id) }
    }

    companion object {
        private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w780"
    }
}