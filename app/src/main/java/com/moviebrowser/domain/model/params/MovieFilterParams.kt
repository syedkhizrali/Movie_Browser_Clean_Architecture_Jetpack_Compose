package com.moviebrowser.domain.model.params

/**
 * Represents filter parameters applied to movie list.
 *
 * Lives in the domain layer because:
 * - It defines business-level filtering logic
 * - It should not depend on presentation layer
 *
 * Passed from UI → ViewModel → UseCase → Repository.
 */
data class MovieFilterParams(

    // Minimum rating threshold (default: no filtering)
    val minRating: Float = 0f,

    // Optional release year filter (null means no filtering)
    val releaseYear: Int? = null,

    // Sorting strategy for the movie list
    val sort: SortParam = SortParam.NONE
)
