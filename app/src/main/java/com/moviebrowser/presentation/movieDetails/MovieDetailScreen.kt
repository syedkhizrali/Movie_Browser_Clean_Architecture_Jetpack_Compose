package com.moviebrowser.presentation.movieDetails

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.moviebrowser.BuildConfig
import com.moviebrowser.R
import com.moviebrowser.core.util.EndPoints
import com.moviebrowser.core.util.FormatUtils.changeDateFormat
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.presentation.components.OfflineBanner
import com.moviebrowser.ui.theme.Accent
import com.moviebrowser.ui.theme.Dimens
import timber.log.Timber

/**
 * Screen responsible for displaying movie details.
 *
 * Handles:
 * - Loading state
 * - Error state
 * - Movie content display
 * - Favourite toggle
 * - Back navigation
 *
 * Collects state from MovieDetailViewModel.
 */
@SuppressLint("DefaultLocale")
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val context = LocalContext.current

    /**
     * Trigger data loading when movieId changes.
     * Also collects favourite toggle messages
     * to show feedback via Toast.
     */
    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)

        viewModel.favFlow.collect {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Collect UI state from ViewModel
    val state by viewModel.movieDetailState.collectAsState()

    when {

        // Loading state
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error state
        state.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.error ?: stringResource(R.string.unknown_error))
            }
        }

        // Success state
        state.movie != null -> {

            val movie = state.movie!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
            ) {

                // Construct full poster image URL
                val imageUrl = movie.posterPath?.let {
                    "${BuildConfig.IMAGE_URL}${EndPoints.POSTER_SIZE_MEDIUM}${it}"
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    /**
                     * Async image loading using Coil.
                     * Shows loading indicator while fetching.
                     */
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens.posterHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        },
                        error = {
                            Image(
                                painter = painterResource(R.drawable.ic_default_img),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.posterHeight)
                    )

                    /**
                     * Top bar overlay:
                     * - Back button
                     * - Favourite toggle button
                     */
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = Dimens.xxxl,
                                start = Dimens.lg,
                                end = Dimens.lg
                            )
                    ) {

                        // Back navigation button
                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = Dimens.hairline),
                            colors = cardColors(containerColor = Color.White),
                            modifier = Modifier.clickable {
                                navController.popBackStack()
                            }
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                                modifier = Modifier
                                    .size(Dimens.section)
                                    .align(Alignment.Start)
                                    .padding(Dimens.inset),
                                contentDescription = stringResource(R.string.back_button),
                                colorFilter = ColorFilter.tint(Color(0xFF1F3A5F))
                            )
                        }

                        // Favourite toggle button
                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = Dimens.hairline),
                            colors = cardColors(containerColor = Color.White),
                            modifier = Modifier.clickable {
                                Timber.d("CLICKED FAV BUTTON")
                                viewModel.toggleFavourite(movie)
                            }
                        ) {
                            Image(
                                imageVector =
                                    if (movie.isFavourite)
                                        ImageVector.vectorResource(id = R.drawable.ic_heart_filled)
                                    else
                                        ImageVector.vectorResource(id = R.drawable.ic_heart_outlined),
                                modifier = Modifier
                                    .size(Dimens.section)
                                    .align(Alignment.Start)
                                    .padding(Dimens.sm),
                                contentDescription = stringResource(R.string.addToFav),
                                colorFilter = ColorFilter.tint(Color.Red)
                            )
                        }
                    }
                }

                // Movie information section
                Column(
                    modifier = Modifier.padding(Dimens.lg)
                ) {

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(Dimens.md))

                    // Rating section
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(Dimens.xl)
                        )

                        Spacer(modifier = Modifier.width(Dimens.inset))

                        Text(
                            text = String.format("%.1f", movie.rating),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.sm))

                    // Release date formatted using extension function
                    Text(
                        text = stringResource(
                            R.string.release_date,
                            movie.releaseDate.changeDateFormat()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(Dimens.lg))

                    // Overview text
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
