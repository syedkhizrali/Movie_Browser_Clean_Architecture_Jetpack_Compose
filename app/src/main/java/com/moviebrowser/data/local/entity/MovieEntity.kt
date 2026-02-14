package com.moviebrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a cached movie item.
 *
 * Stored in the local database to support:
 * - Offline mode
 * - Pagination via RemoteMediator
 * - Local filtering and sorting
 */
@Entity(tableName = "movies")
data class MovieEntity(

    // Unique movie ID from TMDB
    @PrimaryKey val id: Int,

    val title: String,

    val overview: String,

    // Relative poster path returned by API (combined with base URL when displayed)
    val posterPath: String?,

    val rating: Double,

    // Raw release date string from API (formatted later for UI)
    val releaseDate: String?,

    // Page number used by Paging 3 to maintain ordering and pagination state
    val page: Int,
)
