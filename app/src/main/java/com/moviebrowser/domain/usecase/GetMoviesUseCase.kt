package com.moviebrowser.domain.usecase

import androidx.paging.PagingData
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.repository.MovieRepository
import com.moviebrowser.domain.model.params.MovieFilterParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving paginated movies.
 *
 * Acts as a bridge between ViewModel and Repository.
 * Keeps the presentation layer unaware of data source details.
 */
class GetMoviesUseCase @Inject constructor(

    // Repository abstraction injected via Hilt
    private val repository: MovieRepository
) {

    /**
     * Invokes the use case.
     *
     * Returns a Flow of PagingData<Movie> that:
     * - Supports search query
     * - Applies filters (rating, year, sorting)
     * - Enables reactive UI updates
     */
    operator fun invoke(
        query: String,
        filters: MovieFilterParams
    ): Flow<PagingData<Movie>> {
        return repository.getMovies(
            query = query,
            filters = filters
        )
    }
}
