package com.moviebrowser.data.mapper

import com.moviebrowser.data.local.entity.MovieEntity
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.data.local.model.MovieWithFavourites
import com.moviebrowser.data.remote.dto.MovieDto

/**
 * Mapper functions responsible for transforming data
 * between layers:
 *
 * DTO (network) → Entity (database)
 * Entity / DB model → Domain model
 *
 * This keeps the domain layer independent from
 * database and network implementations.
 */


/**
 * Converts a MovieEntity to a Domain model.
 *
 * isFavourite is passed separately because
 * MovieEntity itself does not store favourite state.
 */
fun MovieEntity.toDomain(isFavourite: Boolean): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        rating = rating,
        releaseDate = releaseDate,
        isFavourite = isFavourite
    )
}


/**
 * Converts MovieWithFavourites (result of JOIN query)
 * directly into a Domain model.
 *
 * Used when fetching paginated movies with favourite state
 * computed from LEFT JOIN.
 */
fun MovieWithFavourites.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        rating = rating,
        releaseDate = releaseDate,
        isFavourite = isFavourite
    )
}

/**
 * Converts network DTO into a database Entity.
 *
 * page is injected manually to:
 * - Maintain pagination order
 * - Support RemoteMediator caching logic
 */
fun MovieDto.toEntity(page: Int): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        rating = rating,
        releaseDate = releaseDate,
        page = page
    )
}
