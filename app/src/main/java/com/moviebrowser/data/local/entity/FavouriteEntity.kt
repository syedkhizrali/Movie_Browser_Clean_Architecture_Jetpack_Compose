package com.moviebrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a favourite movie.
 *
 * Stored separately from MovieEntity to:
 * - Keep favourite state decoupled from movie cache
 * - Allow independent CRUD operations
 * - Enable JOIN queries for computing isFavourite dynamically
 */
@Entity(tableName = "favourites")
data class FavouriteEntity(

    /**
     * Unique movie ID.
     *
     * Acts as the primary key to ensure:
     * - No duplicate favourite entries
     * - Fast lookup and deletion
     */
    @PrimaryKey val movieId: Int
)
