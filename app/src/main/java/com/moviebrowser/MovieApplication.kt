package com.moviebrowser

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.moviebrowser.BuildConfig

/**
 * Application class for the app.
 *
 * Responsibilities:
 * - Initializes Hilt dependency injection
 * - Performs global app-level setup
 * - Configures logging
 *
 * Annotated with @HiltAndroidApp to trigger
 * Hilt's code generation.
 */
@HiltAndroidApp
class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * Initialize Timber logging.
         *
         * Debug tree is planted only in debug builds.
         * In a production app, a release logging tree
         * would typically be configured instead.
         */
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
