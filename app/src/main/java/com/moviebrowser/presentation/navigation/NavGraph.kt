package com.moviebrowser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import com.moviebrowser.presentation.movieList.MovieListScreen
import com.moviebrowser.presentation.movieDetails.MovieDetailScreen

/**
 * Central navigation graph for the application.
 *
 * Defines:
 * - Start destination
 * - Navigation routes
 * - Route arguments
 *
 * Keeps navigation logic separated from UI screens.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.MovieListScreen.route
    ) {

        /**
         * Movie list screen (start destination).
         */
        composable(Routes.MovieListScreen.route) {
            MovieListScreen(navController = navController)
        }

        /**
         * Movie detail screen.
         *
         * Accepts movieId as navigation argument.
         */
        composable(
            route = Routes.MovieDetailScreen.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            // Extract movieId from arguments
            val movieId =
                backStackEntry.arguments?.getInt("movieId") ?: 0

            MovieDetailScreen(
                navController = navController,
                movieId = movieId,
            )
        }
    }
}
