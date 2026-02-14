package com.moviebrowser.data.local.model

/**
 * Data class for managing favourites items in the local db of our app
 * */
data class MovieWithFavourites(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val rating: Double,
    val releaseDate: String?,
    val page: Int,
    val isFavourite: Boolean
)
