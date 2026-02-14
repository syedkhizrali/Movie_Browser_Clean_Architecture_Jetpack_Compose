package com.moviebrowser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviebrowser.core.network.NetworkChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel responsible for exposing network connectivity state.
 *
 * Converts NetworkChecker Flow into a StateFlow
 * that can be safely observed by the UI layer.
 *
 * Used to:
 * - Show offline banner
 * - React to connectivity changes
 */
@HiltViewModel
class ConnectivityViewModel @Inject constructor(

    // Observes network connectivity changes
    networkChecker: NetworkChecker
) : ViewModel() {

    /**
     * StateFlow representing current online status.
     *
     * - Converts cold Flow to hot StateFlow
     * - Scoped to ViewModel lifecycle
     * - Starts when subscribed
     * - Stops after 5 seconds without subscribers
     */
    val isOnline = networkChecker.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
}
