package com.moviebrowser.di

import com.moviebrowser.domain.repository.MovieRepository
import com.moviebrowser.data.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module responsible for binding
 * repository implementations to their interfaces.
 *
 * This allows the domain layer to depend on abstractions
 * rather than concrete implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds MovieRepositoryImpl to MovieRepository interface.
     *
     * Ensures:
     * - Clean architecture principles
     * - Easier testing and mocking
     * - Proper dependency inversion
     */
    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository
}
