package com.moviebrowser.domain.usecase

import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case responsible for toggling favourite state of a movie.
 *
 * Encapsulates the business rule:
 * - If movie is already favourite → remove it
 * - If not → add it
 *
 * Keeps this decision logic out of the ViewModel.
 */
class UpdateFavouriteUseCase @Inject constructor(

    // Repository abstraction injected via Hilt
    private val repository: MovieRepository
) {

    /**
     * Toggles favourite state based on current value.
     *
     * The Movie model contains the current isFavourite state,
     * which determines whether we insert or delete.
     */
    suspend operator fun invoke(movie: Movie) {

        if (movie.isFavourite) {

            // If already favourite, remove it
            repository.removeFromFavourites(movie.id)

        } else {

            // If not favourite, add it
            repository.addToFavourites(movie.id)
        }
    }
}
