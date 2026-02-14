package com.moviebrowser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moviebrowser.data.local.entity.RemoteKeys

/**
 * DAO responsible for handling RemoteKeys used in Paging 3.
 *
 * RemoteKeys are typically used by RemoteMediator to:
 * - Track previous and next page indices
 * - Maintain pagination state in the local database
 */
@Dao
interface RemoteKeysDao {

    /**
     * Retrieves RemoteKeys for a specific movie.
     *
     * Used by RemoteMediator to determine:
     * - Whether to load previous page
     * - Whether to load next page
     */
    @Query("SELECT * FROM remote_keys WHERE movieId = :movieId")
    suspend fun getRemoteKeys(movieId: Int): RemoteKeys?

    /**
     * Inserts or updates RemoteKeys entries.
     *
     * REPLACE strategy ensures:
     * - Pagination state remains consistent
     * - No duplicate keys for same movieId
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    /**
     * Clears all RemoteKeys.
     *
     * Typically called when refreshing data
     * to reset pagination state.
     */
    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
