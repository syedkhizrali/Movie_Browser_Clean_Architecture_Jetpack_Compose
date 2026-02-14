package com.moviebrowser.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moviebrowser.data.local.entity.MovieEntity
import com.moviebrowser.data.local.model.MovieWithFavourites

/**
 * DAO responsible for handling movie-related database operations.
 *
 * Supports:
 * - Pagination with filtering & sorting
 * - Insert operations
 * - Clearing cache
 * - Fetching single movie details
 */
@Dao
interface MovieDao {

    /**
     * Returns a PagingSource for paginated movie list.
     *
     * Features supported:
     * - Search by title (case-insensitive)
     * - Minimum rating filter
     * - Release year filter
     * - Dynamic sorting (rating or release date)
     * - Favourite status via LEFT JOIN
     *
     * LEFT JOIN ensures:
     * - All movies are returned
     * - isFavourite flag is computed dynamically
     */
    @Query("""
SELECT movies.*,
       CASE WHEN favourites.movieId IS NOT NULL THEN 1 ELSE 0 END as isFavourite
FROM movies
LEFT JOIN favourites
ON movies.id = favourites.movieId
WHERE (:query = '' OR LOWER(title) LIKE '%' || LOWER(:query) || '%')
AND rating >= :minRating
AND (:releaseYear IS NULL OR substr(releaseDate, 1, 4) = :releaseYear)
ORDER BY
    CASE WHEN :sort = 'RATING_DESC' THEN rating END DESC,
    CASE WHEN :sort = 'RELEASE_DATE_DESC' THEN releaseDate END DESC,
    page ASC
""")
    fun getMoviesPagingSource(
        query: String,
        minRating: Float,
        releaseYear: String?,
        sort: String
    ): PagingSource<Int, MovieWithFavourites>


    /**
     * Inserts a list of movies into the database.
     *
     * REPLACE strategy ensures:
     * - Existing movies with same primary key are updated.
     * - No duplicate entries.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovie(movies: List<MovieEntity>)

    /**
     * Inserts or updates a single movie entry.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    /**
     * Clears entire movie table.
     *
     * Typically used when refreshing data from API
     * to prevent stale entries.
     */
    @Query("DELETE FROM movies")
    suspend fun clearAll()

    /**
     * Fetches a single movie by its ID.
     *
     * Returns null if movie is not found in local cache.
     */
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?



}
