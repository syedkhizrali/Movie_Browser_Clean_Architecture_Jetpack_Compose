package com.moviebrowser.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO representing the response structure of the movie list API from TMDB.
 *
 * Contains:
 * - Pagination metadata (page, totalPages, totalResults)
 * - List of movie items returned for the current page
 *
 * This model mirrors the remote API response and is mapped
 * to database entities before being used in the domain layer.
 */
@JsonClass(generateAdapter = true)
data class MovieListResponse(

    @field:Json(name = "page")
    val page: Int,

    // List of movies returned for the requested page
    @field:Json(name = "results")
    val results: List<MovieDto>,

    @field:Json(name = "total_pages")
    val totalPages: Int,

    @field:Json(name = "total_results")
    val totalResults: Int
)
