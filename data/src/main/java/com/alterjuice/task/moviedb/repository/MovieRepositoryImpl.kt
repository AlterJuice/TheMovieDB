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

/**
 * Concrete implementation of [MovieRepository], handling movie data operations using a provided [MovieApiService]
 * for remote data and [AppDatabase] for local persistence. Manages pagination, favorites, and data conversion.
 * 
 * @property apiService The service for fetching remote movie data.
 * @property database The local database for caching and favorite movie storage.
 */
internal class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val database: AppDatabase
) : MovieRepository {

    /**
     * Fetches paginated movie data using a [Pager] configured with remote mediating.
     * Maps database entities to domain [Movie] models using [toDomain].
     * 
     * This method combines remote and local data sources, with the remote mediator
     * handling pagination and caching. Placeholders are disabled to prevent empty items.
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = MoviesRemoteMediator(apiService, database),
            pagingSourceFactory = { database.movieDao().pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { movieEntity -> movieEntity.toDomain() }
        }
    }

    /**
     * Returns a flow of favorite movies stored in the local database.
     * The result is converted to an immutable list of domain [Movie] models.
     */
    override fun getFavoriteMovies(): Flow<ImmutableList<Movie>> {
        return database.movieDao().getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }.toImmutableList()
        }
    }

    /**
     * Marks a movie with the given [movieId] as non-favorite in the local database.
     * 
     * @param movieId The ID of the movie to remove from favorites.
     */
    override suspend fun removeFromFavorites(movieId: Int) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite = false)
    }

    /**
     * Marks a movie with the given [movieId] as favorite in the local database.
     * 
     * @param movieId The ID of the movie to add to favorites.
     */
    override suspend fun addToFavorites(movieId: Int) {
        database.movieDao().updateFavoriteStatus(movieId, isFavorite = true)
    }

}
