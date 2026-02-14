package com.moviebrowser.presentation.movieDetails

import com.moviebrowser.domain.model.Movie
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * UI state holder for MovieDetailScreen.
 *
 * Represents all possible states of the screen:
 * - Loading
 * - Success (movie available)
 * - Error
 *
 * Used by the ViewModel and observed by the Composable.
 */
data class MovieDetailState(

    // Indicates whether data is currently being loaded
    val isLoading: Boolean = false,

    // Holds movie details when successfully loaded
    val movie: Movie? = null,

    // Holds error message if something goes wrong
    val error: String? = null,
)
