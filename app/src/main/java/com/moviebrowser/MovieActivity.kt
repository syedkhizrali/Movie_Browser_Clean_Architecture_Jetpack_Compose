package com.moviebrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.moviebrowser.presentation.components.OfflineBanner
import com.moviebrowser.presentation.navigation.AppNavGraph
import com.moviebrowser.ui.theme.MovieBrowserTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the application.
 *
 * Responsibilities:
 * - Entry point for the app
 * - Applies app theme
 * - Sets root composable
 * - Enables edge-to-edge UI
 *
 * Annotated with @AndroidEntryPoint for Hilt dependency injection.
 */
@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MovieActivity : ComponentActivity() {
    private var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge drawing behind system bars
        enableEdgeToEdge()
        //first press will show toast
        //second press within 2 second  will exit the app

        onBackPressedDispatcher.addCallback(this) {

            if (System.currentTimeMillis() - backPressedTime < 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(
                    this@MovieActivity,
                    getString(R.string.press_back_again_to_exit),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        setContent {
            MovieBrowserTheme {
                AppRoot()
            }
        }
    }
}

/**
 * Root composable of the application.
 *
 * Responsible for:
 * - Hosting navigation graph
 * - Observing connectivity state
 * - Displaying global offline banner
 */
@Composable
fun AppRoot() {

    // Obtain ConnectivityViewModel using Hilt
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()

    // Observe online/offline state
    val isOnline by connectivityViewModel.isOnline.collectAsState()

    Box(Modifier.fillMaxSize()) {

        // Main navigation graph
        AppNavGraph()

        // Global offline banner overlay
        if (!isOnline) {
            OfflineBanner()
        }
    }
}
