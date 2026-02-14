package com.moviebrowser.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moviebrowser.data.local.dao.FavouriteDao
import com.moviebrowser.data.local.dao.MovieDao
import com.moviebrowser.data.local.dao.RemoteKeysDao
import com.moviebrowser.data.local.entity.FavouriteEntity
import com.moviebrowser.data.local.entity.MovieEntity
import com.moviebrowser.data.local.entity.RemoteKeys

/**
 * Main Room database class for the application.
 *
 * Contains:
 * - MovieEntity (cached movie list)
 * - RemoteKeys (pagination state for Paging 3)
 * - FavouriteEntity (saved favourite movies)
 *
 * Acts as the central access point to all DAOs.
 */
@Database(
    entities = [MovieEntity::class,
        RemoteKeys::class,
        FavouriteEntity::class],
    version = 2, // Database version incremented when schema changes
    exportSchema = false, // Schema export disabled (can be enabled for production tracking)
)
abstract class MovieDatabase : RoomDatabase() {

    /**
     * Provides access to MovieDao for movie-related operations.
     */
    abstract fun movieDao(): MovieDao

    /**
     * Provides access to RemoteKeysDao for pagination state management.
     */
    abstract fun remoteKeysDao(): RemoteKeysDao

    /**
     * Provides access to FavouriteDao for favourite CRUD operations.
     */
    abstract fun favouriteDao(): FavouriteDao
}
