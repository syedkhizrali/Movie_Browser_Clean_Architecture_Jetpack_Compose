package com.moviebrowser.core.network

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for observing network connectivity state.
 *
 * This interface allows the rest of the app (ViewModels, repositories, etc.)
 * to depend on an abstraction rather than a concrete Android implementation.
 *
 * Exposes connectivity status as a Flow so it can be collected reactively.
 */
interface NetworkObserver {

    /**
     * Emits true when the device has internet connectivity
     * and false when it does not.
     *
     * Using Flow enables reactive UI updates and lifecycle-aware collection.
     */
    val isOnline: Flow<Boolean>
}
