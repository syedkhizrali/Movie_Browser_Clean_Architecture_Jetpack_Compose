package com.moviebrowser.domain.model.params

/**
 * Represents available sorting strategies
 * for the movie list.
 *
 * Kept in the domain layer because sorting
 * is considered part of business logic,
 * not a UI concern.
 */
enum class SortParam {

    // No specific sorting applied
    NONE,

    // Sort movies by rating
    RATING,

    // Sort movies by release date
    RELEASE_DATE
}
