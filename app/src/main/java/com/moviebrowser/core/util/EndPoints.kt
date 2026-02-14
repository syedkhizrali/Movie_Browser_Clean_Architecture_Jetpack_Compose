package com.moviebrowser.core.util

/**
 * Central place for API endpoint paths and related constants.
 *
 * Keeping endpoints in a separate object improves maintainability
 * and avoids hardcoding strings throughout the network layer.
 */
object EndPoints {

    // Endpoint for fetching movie list (used with query parameters like page, filters, etc.)
    const val MOVIE_LIST_URL = "discover/movie"

    // Endpoint for fetching movie details.
    // {movie_id} will be replaced dynamically when making the request.
    const val MOVIE_DETAIL_URL = "movie/{movie_id}"


    // Image Sizes

    // Default poster size used when constructing image URLs
    const val POSTER_SIZE_MEDIUM = "w500"
}
