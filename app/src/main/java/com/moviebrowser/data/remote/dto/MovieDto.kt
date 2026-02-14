package com.moviebrowser.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO that represents the data we're fetching from the TMDB api
 * this represents one item of movie that we're getting in the list.
 * This same class is being used for details as fields we're using in
 * both are exact same
 * */
@JsonClass(generateAdapter = true)
data class MovieDto(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "overview")
    val overview: String,

    @field:Json(name = "poster_path")
    val posterPath: String?,

    @field:Json(name = "vote_average")
    val rating: Double,

    @field:Json(name = "release_date")
    val releaseDate: String
)