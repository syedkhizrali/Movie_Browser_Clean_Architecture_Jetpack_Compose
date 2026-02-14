package com.moviebrowser.presentation.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.usecase.GetMovieDetailsUseCase
import com.moviebrowser.domain.usecase.UpdateFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing MovieDetailScreen state.
 *
 * Handles:
 * - Loading movie details
 * - Updating favourite state
 * - Emitting UI events (Toast messages)
 *
 * Uses:
 * - StateFlow for UI state
 * - SharedFlow for one-time events
 */
@HiltViewModel
class MovieDetailViewModel @Inject constructor(

    // Use case for fetching movie details
    private val movieDetailsUseCase: GetMovieDetailsUseCase,

    // Use case for toggling favourite state
    private val updateFavouriteUseCase: UpdateFavouriteUseCase,
) : ViewModel() {

    /**
     * Backing StateFlow for screen state.
     */
    private val _movieDetailState = MutableStateFlow(MovieDetailState())

    // Public immutable state exposed to UI
    val movieDetailState = _movieDetailState.asStateFlow()

    /**
     * SharedFlow used for one-time UI events (e.g., Toast messages).
     */
    val favFlow: MutableSharedFlow<String> = MutableSharedFlow()

    // Exposed as read-only SharedFlow
    val favState = favFlow.asSharedFlow()

    /**
     * Loads movie details by ID.
     *
     * Updates state to:
     * - Loading
     * - Success
     * - Error
     */
    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {

            // Set loading state
            _movieDetailState.value = MovieDetailState(isLoading = true)

            val movie = movieDetailsUseCase(movieId)

            if (movie != null) {

                // Success state
                _movieDetailState.value = MovieDetailState(movie = movie)

            } else {

                // Error state
                _movieDetailState.value = MovieDetailState(
                    error = "Unable to load movie details"
                )
            }
        }
    }

    /**
     * Toggles favourite state for a movie.
     *
     * Steps:
     * 1. Execute update favourite use case
     * 2. Re-fetch updated movie details
     * 3. Update UI state
     * 4. Emit feedback event
     */
    fun toggleFavourite(movie: Movie) {
        viewModelScope.launch {

            val isFav = movie.isFavourite

            // Update favourite status
            updateFavouriteUseCase(movie)

            // Fetch updated movie (with new favourite state)
            val updatedMovie = movieDetailsUseCase(movie.id)

            // Update UI state with refreshed movie
            _movieDetailState.value =
                _movieDetailState.value.copy(movie = updatedMovie)

            // Emit one-time UI message
            favFlow.emit(
                if (isFav) {
                    "Removed from favourites"
                } else {
                    "Added to favourites"
                }
            )
        }
    }
}
