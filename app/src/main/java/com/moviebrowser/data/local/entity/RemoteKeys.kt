package com.moviebrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity used by Paging 3 RemoteMediator to track pagination state.
 *
 * Each movie item stores:
 * - Previous page key
 * - Next page key
 *
 * This enables proper append, prepend, and refresh behavior
 * in an offline-first pagination setup.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeys(

    // Unique movie ID associated with this pagination key
    @PrimaryKey val movieId: Int,

    // Previous page index (null if this is the first page)
    val prevKey: Int?,

    // Next page index (null if this is the last page)
    val nextKey: Int?
)
