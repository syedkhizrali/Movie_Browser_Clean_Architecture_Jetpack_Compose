package com.moviebrowser.domain.usecase

import com.moviebrowser.domain.model.Movie
import com.moviebrowser.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Use case responsible for retrieving movie details.
 *
 * Acts as an abstraction between ViewModel and Repository.
 * Keeps business logic inside the domain layer.
 */
class GetMovieDetailsUseCase @Inject constructor(

    // Repository abstraction injected via Hilt
    private val repository: MovieRepository
) {

    /**
     * Invokes the use case.
     *
     * Returns movie details by ID.
     * Delegates data fetching responsibility to repository.
     */
    suspend operator fun invoke(id: Int): Movie? {
        return repository.getMovieById(id)
    }
}
