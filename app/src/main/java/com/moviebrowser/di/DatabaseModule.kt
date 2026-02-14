package com.moviebrowser.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moviebrowser.data.local.dao.FavouriteDao
import com.moviebrowser.data.local.dao.MovieDao
import com.moviebrowser.data.local.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module responsible for providing
 * Room database and DAO dependencies.
 *
 * Installed in SingletonComponent to ensure
 * single instance throughout app lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Migration from version 1 to version 2.
     *
     * Adds the "favourites" table without
     * deleting existing movie data.
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {

            // Create favourites table manually during schema upgrade
            db.execSQL("""
            CREATE TABLE IF NOT EXISTS favourites (
                movieId INTEGER NOT NULL,
                PRIMARY KEY(movieId)
            )
        """.trimIndent())
        }
    }

    /**
     * Provides singleton instance of MovieDatabase.
     *
     * Uses Room.databaseBuilder with migration support
     * to preserve user data during upgrades.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    /**
     * Provides MovieDao dependency.
     */
    @Provides
    fun provideMovieDao(
        database: MovieDatabase
    ): MovieDao = database.movieDao()

    /**
     * Provides FavouriteDao dependency.
     */
    @Provides
    fun provideFavouriteDao(
        database: MovieDatabase
    ): FavouriteDao = database.favouriteDao()
}
