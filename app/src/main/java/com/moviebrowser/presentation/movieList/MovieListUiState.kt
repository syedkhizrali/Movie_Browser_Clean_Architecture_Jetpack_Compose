package com.moviebrowser.presentation.movieList

import com.moviebrowser.domain.model.params.MovieFilterParams

/**
 * UI state holder for MovieListScreen.
 *
 * Represents:
 * - Current search query
 * - Active filter parameters
 * - Filter bottom sheet visibility state
 *
 * Observed by the Composable and updated by ViewModel.
 */
data class MovieListUiState(

    // Current search text entered by user
    val searchQuery: String = "",

    // Active filter configuration applied to movie list
    val filters: MovieFilterParams = MovieFilterParams(),

    // Controls visibility of filter bottom sheet
    val isFilterSheetOpen: Boolean = false,
)
