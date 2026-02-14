package com.moviebrowser.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.moviebrowser.core.network.NetworkObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [NetworkObserver].
 *
 * Responsible for observing real-time network connectivity changes
 * and exposing them as a cold Flow<Boolean>.
 *
 * Uses callbackFlow to bridge Android's callback-based API
 * (ConnectivityManager.NetworkCallback) into Kotlin Flow.
 */
@Singleton
class NetworkChecker @Inject constructor(
    // Application context is injected to avoid leaking Activity/Fragment context
    @ApplicationContext private val context: Context
) : NetworkObserver {

    // System service used to monitor network state
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Emits true when internet becomes available,
     * and false when it is lost.
     *
     * This Flow stays active while being collected and
     * automatically unregisters the callback when cancelled.
     */
    override val isOnline: Flow<Boolean> = callbackFlow {

        // Callback that listens to network availability changes
        val callback = object : ConnectivityManager.NetworkCallback() {

            // Triggered when a network with internet capability becomes available
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            // Triggered when the active network is lost
            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        // Request specifically for networks that have internet capability
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Register the callback to start listening for changes
        connectivityManager.registerNetworkCallback(request, callback)

        // Emit the current connection state immediately
        // so UI does not wait for a network change event
        trySend(checkCurrentConnection())

        // Ensures callback is unregistered when Flow collection stops
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    /**
     * Checks current active network synchronously.
     *
     * Used to emit initial connectivity state before
     * waiting for callback updates.
     */
    private fun checkCurrentConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
