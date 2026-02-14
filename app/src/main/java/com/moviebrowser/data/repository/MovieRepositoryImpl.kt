package com.moviebrowser.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.moviebrowser.data.local.database.MovieDatabase
import com.moviebrowser.data.local.entity.FavouriteEntity
import com.moviebrowser.data.mapper.toDomain
import com.moviebrowser.data.mapper.toEntity
import com.moviebrowser.data.remote.api.MovieApi
import com.moviebrowser.data.remote.paging.MoviesRemoteMediator
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.repository.MovieRepository
import com.moviebrowser.domain.model.params.MovieFilterParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Concrete implementation of MovieRepository.
 *
 * Acts as the single source of truth for movie data.
 * Combines:
 * - Remote API
 * - Local database
 * - Paging 3 with RemoteMediator
 *
 * Keeps domain layer independent of data sources.
 */
@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val database: MovieDatabase
) : MovieRepository {

    /**
     * Returns paginated list of movies as Flow<PagingData<Movie>>.
     *
     * Supports:
     * - Search query
     * - Filters (rating, year, sort)
     * - Offline caching via RemoteMediator
     */
    override fun getMovies(
        query: String,
        filters: MovieFilterParams
    ): Flow<PagingData<Movie>> {

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),

            // RemoteMediator enables API + DB sync
            remoteMediator = MoviesRemoteMediator(api, database),

            // Local paging source with filters applied
            pagingSourceFactory = {
                database.movieDao().getMoviesPagingSource(
                    query = query,
                    minRating = filters.minRating,
                    releaseYear = filters.releaseYear?.toString(),
                    sort = filters.sort.name
                )
            }
        ).flow.map { pagingData ->

            // Convert DB model into Domain model
            pagingData.map { movieWithFav ->
                movieWithFav.toDomain()
            }
        }
    }

    /**
     * Fetches movie details by ID.
     *
     * Strategy:
     * 1. Try fetching from API
     * 2. Cache result locally
     * 3. If API fails, fallback to local database
     */
    override suspend fun getMovieById(id: Int): Movie? {

        val movieDao = database.movieDao()
        val favouriteDao = database.favouriteDao()

        return try {

            // Attempt to fetch latest data from API
            val remote = api.getMovieDetails(id)

            // Preserve existing page number if movie already cached
            val existing = movieDao.getMovieById(id)
            val page = existing?.page ?: 1

            // Convert DTO to Entity
            val entity = remote.toEntity(page = page)

            // Update local cache
            movieDao.insertMovie(entity)

            // Retrieve favourite state separately
            val isFavourite = favouriteDao.isFavourite(id)

            // Convert to Domain model
            entity.toDomain(isFavourite)

        } catch (e: Exception) {

            // If network/API fails, fallback to cached data
            Timber.e(e)

            val local = movieDao.getMovieById(id)
            val isFavourite = favouriteDao.isFavourite(id)

            local?.toDomain(isFavourite)
        }
    }

    /**
     * Adds a movie to favourites table.
     */
    override suspend fun addToFavourites(id: Int) {
        database.favouriteDao().add(FavouriteEntity(id))
    }

    /**
     * Removes a movie from favourites table.
     */
    override suspend fun removeFromFavourites(id: Int) {
        database.favouriteDao().remove(id)
    }
}
