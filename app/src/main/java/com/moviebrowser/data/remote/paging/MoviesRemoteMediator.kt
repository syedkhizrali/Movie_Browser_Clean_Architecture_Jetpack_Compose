package com.moviebrowser.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.moviebrowser.data.local.database.MovieDatabase
import com.moviebrowser.data.local.entity.MovieEntity
import com.moviebrowser.data.local.entity.RemoteKeys
import com.moviebrowser.data.remote.api.MovieApi
import com.moviebrowser.data.local.model.MovieWithFavourites
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * RemoteMediator implementation for Paging 3.
 *
 * Responsible for:
 * - Fetching paginated data from remote API
 * - Storing it in local database
 * - Managing pagination state using RemoteKeys
 *
 * Enables offline-first paging behavior.
 */
@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val api: MovieApi,
    private val database: MovieDatabase
) : RemoteMediator<Int, MovieWithFavourites>() {

    // DAO references for database operations
    private val movieDao = database.movieDao()
    private val remoteKeysDao = database.remoteKeysDao()

    /**
     * Called by Paging when more data needs to be loaded.
     *
     * loadType determines whether:
     * - REFRESH (initial load or refresh)
     * - PREPEND (load before current data)
     * - APPEND (load after current data)
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieWithFavourites>
    ): MediatorResult {

        // Determine which page to load based on LoadType
        val page = when (loadType) {

            LoadType.REFRESH -> {
                // Try to restore page near current position
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                nextKey
            }
        }

        try {

            // Fetch movies from API
            val response = api.getMovies(
                page = page,
                sortBy = "popularity.desc",
                releaseYear = null,
                minRating = null
            )

            val movies = response.results
            val endOfPaginationReached = movies.isEmpty()

            // Database operations executed atomically
            database.withTransaction {

                // On refresh, clear old cache and keys
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    movieDao.clearAll()
                }

                // Create pagination keys for each movie
                val keys = movies.map {
                    RemoteKeys(
                        movieId = it.id,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }

                remoteKeysDao.insertAll(keys)

                // Convert DTOs to entities for local storage
                val entities = movies.map { dto ->
                    MovieEntity(
                        id = dto.id,
                        title = dto.title,
                        overview = dto.overview,
                        posterPath = dto.posterPath,
                        rating = dto.rating,
                        releaseDate = dto.releaseDate,
                        page = page
                    )
                }

                movieDao.insertAllMovie(entities)
            }

            return MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )

        } catch (e: IOException) {
            // Network error
            Timber.e(e)
            return MediatorResult.Success(
                endOfPaginationReached = true
            )
        } catch (e: HttpException) {
            // API error (4xx / 5xx)
            return MediatorResult.Error(e)
        }

    }

    /**
     * Gets RemoteKeys for the last item in the current PagingState.
     * Used when appending data.
     */
    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieWithFavourites>
    ): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie ->
                remoteKeysDao.getRemoteKeys(movie.id)
            }
    }

    /**
     * Gets RemoteKeys for the first item in the current PagingState.
     * Used when prepending data.
     */
    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieWithFavourites>
    ): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie ->
                remoteKeysDao.getRemoteKeys(movie.id)
            }
    }

    /**
     * Gets RemoteKeys closest to the current scroll position.
     * Used during refresh to maintain correct page position.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieWithFavourites>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeys(id)
            }
        }
    }
}
