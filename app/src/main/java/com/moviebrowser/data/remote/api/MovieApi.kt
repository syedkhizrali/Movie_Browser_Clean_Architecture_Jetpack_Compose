package com.moviebrowser.data.remote.api

import com.moviebrowser.core.util.EndPoints
import com.moviebrowser.data.remote.dto.MovieDto
import com.moviebrowser.data.remote.dto.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API interface defining all movie-related network calls.
 *
 * Acts as the remote data source contract.
 * Implementation is generated automatically by Retrofit.
 */
interface MovieApi {

    /**
     * Fetches a paginated list of movies.
     *
     * Supports:
     * - Page number (for pagination)
     * - Sorting
     * - Release year filtering
     * - Minimum rating filtering
     *
     * Default sorting is by popularity in descending order.
     */
    @GET(EndPoints.MOVIE_LIST_URL)
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("primary_release_year") releaseYear: Int?,
        @Query("vote_average.gte") minRating: Float?
    ): MovieListResponse

    /**
     * Fetches detailed information for a specific movie.
     *
     * movieId is injected into the endpoint path dynamically.
     * Language defaults to English.
     */
    @GET(EndPoints.MOVIE_DETAIL_URL)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDto
}
