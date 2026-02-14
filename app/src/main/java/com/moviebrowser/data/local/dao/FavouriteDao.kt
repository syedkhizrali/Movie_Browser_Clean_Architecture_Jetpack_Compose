package com.moviebrowser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moviebrowser.data.local.entity.FavouriteEntity

/**
 * Data Access Object (DAO) for managing favourite movies in Room database.
 *
 * Responsible for performing CRUD operations related to favourites.
 * This acts as the direct interface between the database and repository layer.
 */
@Dao
interface FavouriteDao {

    /**
     * Inserts a movie into favourites.
     *
     * OnConflictStrategy.REPLACE ensures:
     * - If the movie already exists (same primary key),
     *   it will be replaced instead of causing a crash.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favourite: FavouriteEntity)

    /**
     * Removes a movie from favourites using its movieId.
     */
    @Query("DELETE FROM favourites WHERE movieId = :id")
    suspend fun remove(id: Int)

    /**
     * Checks whether a movie is marked as favourite.
     *
     * Returns true if a record exists, otherwise false.
     * Uses SQL EXISTS for efficient boolean check.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE movieId = :id)")
    suspend fun isFavourite(id: Int): Boolean
}
