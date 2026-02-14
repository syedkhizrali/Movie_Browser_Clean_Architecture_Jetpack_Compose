package com.moviebrowser.domain.repository

import androidx.paging.PagingData
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.model.params.MovieFilterParams
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for movie-related operations.
 *
 * Defines what the domain layer expects,
 * without exposing how the data is fetched or stored.
 *
 * The implementation may combine:
 * - Remote API
 * - Local database
 * - Caching logic
 * - Pagination
 */
interface MovieRepository {

    /**
     * Returns a paginated stream of movies.
     *
     * Supports:
     * - Search query
     * - Filtering (rating, year, sorting)
     *
     * Exposes PagingData wrapped inside Flow
     * for reactive UI updates.
     */
    fun getMovies(
        query: String,
        filters: MovieFilterParams
    ): Flow<PagingData<Movie>>

    /**
     * Fetches detailed information for a specific movie.
     *
     * May retrieve from:
     * - Remote API
     * - Local cache (fallback)
     */
    suspend fun getMovieById(id: Int): Movie?

    /**
     * Adds a movie to favourites.
     */
    suspend fun addToFavourites(id: Int)

    /**
     * Removes a movie from favourites.
     */
    suspend fun removeFromFavourites(id: Int)
}
