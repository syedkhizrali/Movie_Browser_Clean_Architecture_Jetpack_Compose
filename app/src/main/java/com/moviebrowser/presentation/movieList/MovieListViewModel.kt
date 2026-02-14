package com.moviebrowser.presentation.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.moviebrowser.core.network.NetworkChecker
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.usecase.GetMoviesUseCase
import com.moviebrowser.domain.usecase.UpdateFavouriteUseCase
import com.moviebrowser.domain.model.params.MovieFilterParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel responsible for managing MovieListScreen state.
 *
 * Handles:
 * - Search query changes
 * - Filter updates
 * - Pagination flow
 * - Favourite toggling
 *
 * Uses reactive Flow combination to automatically
 * refresh movie list when search or filters change.
 */
@HiltViewModel
class MovieListViewModel @Inject constructor(

    // Use case for retrieving paginated movies
    private val getMoviesUseCase: GetMoviesUseCase,

    // Use case for updating favourite state
    private val updateFavouriteUseCase: UpdateFavouriteUseCase,
) : ViewModel() {

    /**
     * Backing state for UI state.
     */
    private val _movieListUiState = MutableStateFlow(MovieListUiState())

    // Public immutable state exposed to UI
    val movieListUiState = _movieListUiState.asStateFlow()

    /**
     * Reactive movie list Flow.
     *
     * Combines:
     * - Search query (debounced)
     * - Filter parameters
     *
     * Any change automatically triggers a new paging request.
     */
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies = combine(

        // Observe search query with debounce
        movieListUiState
            .map { it.searchQuery }
            .debounce(400)
            .distinctUntilChanged(),

        // Observe filter changes
        movieListUiState
            .map { it.filters }
            .distinctUntilChanged()

    ) { query, filters ->

        // Combine both into a Pair
        query to filters

    }
        // Switch to new paging source when query or filters change
        .flatMapLatest { (query, filters) ->
            getMoviesUseCase(query, filters)
        }

        // Cache paging results inside ViewModel scope
        .cachedIn(viewModelScope)

    /**
     * Updates search query.
     *
     * Triggers new paging flow due to combine logic.
     */
    fun onSearchChanged(query: String) {
        _movieListUiState.update {
            it.copy(searchQuery = query)
        }
    }

    /**
     * Opens filter bottom sheet.
     */
    fun onFilterClicked() {
        _movieListUiState.update {
            it.copy(isFilterSheetOpen = true)
        }
    }

    /**
     * Applies selected filters and closes bottom sheet.
     *
     * Triggers paging refresh automatically.
     */
    fun onFiltersApplied(filters: MovieFilterParams) {
        Timber.d("FILTERS $filters")
        _movieListUiState.update {
            it.copy(
                filters = filters,
                isFilterSheetOpen = false
            )
        }
    }

    /**
     * Toggles favourite state of a movie.
     *
     * Delegates logic to use case.
     * Paging will reflect changes automatically
     * because database emits updated data.
     */
    fun toggleFavourite(movie: Movie) {
        Timber.d("FAV ${movie.isFavourite}")
        viewModelScope.launch {
            updateFavouriteUseCase(movie)
        }
    }
}
