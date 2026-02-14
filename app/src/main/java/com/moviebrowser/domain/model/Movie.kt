package com.moviebrowser.domain.model

/**
 * Domain model representing a Movie.
 *
 * This model is used by the presentation layer.
 * It is independent of:
 * - Database entities
 * - Network DTOs
 *
 * Acts as the clean representation of movie data
 * inside the app's business logic.
 */
data class Movie(

    val id: Int,

    val title: String,

    val overview: String,

    val posterPath: String?,

    val rating: Double,

    val releaseDate: String?,

    // Indicates whether the movie is marked as favourite locally
    val isFavourite: Boolean = false
)
