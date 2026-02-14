package com.moviebrowser.presentation.navigation

/**
 * Defines all navigation routes used in the app.
 *
 * Using a sealed class:
 * - Prevents hardcoded route strings
 * - Centralizes navigation paths
 * - Improves maintainability
 */
sealed class Routes(val route: String) {

    /**
     * Route for Movie List screen.
     * Acts as the start destination.
     */
    object MovieListScreen : Routes("movie_list")

    /**
     * Route for Movie Detail screen.
     *
     * Expects movieId as navigation argument.
     */
    object MovieDetailScreen : Routes("movie_detail/{movieId}") {

        /**
         * Helper function to construct route
         * with actual movieId.
         */
        fun createRoute(movieId: Int): String {
            return "movie_detail/$movieId"
        }
    }
}
