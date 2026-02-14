package com.moviebrowser.presentation.movieList

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.moviebrowser.BuildConfig
import com.moviebrowser.ConnectivityViewModel
import com.moviebrowser.R
import com.moviebrowser.core.util.EndPoints
import com.moviebrowser.core.util.FormatUtils.changeDateFormat
import com.moviebrowser.domain.model.Movie
import com.moviebrowser.presentation.components.NoData
import com.moviebrowser.presentation.components.OfflineBanner
import com.moviebrowser.presentation.components.ProgressIndicator
import com.moviebrowser.presentation.movieList.filter.FilterBottomSheet
import com.moviebrowser.ui.theme.Accent
import com.moviebrowser.ui.theme.Dimens
import com.moviebrowser.ui.theme.Primary


/**
 * Main screen displaying paginated movie list.
 *
 * Responsibilities:
 * - Collect paging data from ViewModel
 * - Handle search input
 * - Open filter bottom sheet
 * - Show loading / error / empty states
 * - Navigate to movie details
 */
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    navController: NavHostController
) {

    // Collect paginated data from ViewModel
    val movieData = viewModel.movies.collectAsLazyPagingItems()

    // Used to dismiss keyboard after search
    val focusManager = LocalFocusManager.current

    // Collect UI state (search, filters, bottom sheet state)
    val movieListUiState by viewModel.movieListUiState.collectAsState()

    // Obtain ConnectivityViewModel using Hilt
    val connectivityViewModel: ConnectivityViewModel = hiltViewModel()

    // Observe online/offline state
    val isOnline by connectivityViewModel.isOnline.collectAsState()

    //when app comes back online, refreshes the data so app
    // can utilise data from api as well
    LaunchedEffect(isOnline) {
        if (isOnline) {
            movieData.refresh()
        }
    }
    /**
     * Show filter bottom sheet if requested.
     */
    if (movieListUiState.isFilterSheetOpen) {
        FilterBottomSheet(
            currentFilters = movieListUiState.filters,
            onApply = { filters ->
                viewModel.onFiltersApplied(filters)
            },
            onDismiss = {
                viewModel.onFiltersApplied(movieListUiState.filters)
            }
        )
    }

    Column(
        modifier = Modifier.padding(
            start = Dimens.lg,
            end = Dimens.lg
        ),
    ) {

        /**
         * App banner section (logo + app name).
         */
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.lg)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .size(Dimens.bannerHeight)
                    .align(Alignment.CenterVertically),
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .offset(x = Dimens.offset)
                    .padding(start = Dimens.lg)
                    .align(Alignment.CenterVertically)
            )
        }

        /**
         * Search bar + Filter button row.
         */
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(Dimens.largeSection)
                .fillMaxWidth()
        ) {

            // Search input field
            OutlinedTextField(
                modifier = Modifier
                    .border(
                        shape = CircleShape,
                        border = BorderStroke(
                            width = Dimens.stroke,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    .weight(1f),
                value = movieListUiState.searchQuery,
                onValueChange = {
                    viewModel.onSearchChanged(it)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.onSearchChanged(movieListUiState.searchQuery)
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(R.string.search_btn_content),
                        modifier = Modifier
                            .size(Dimens.iconMedium27)
                            .padding(Dimens.inset)
                    )
                },
                shape = CircleShape,
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                placeholder = {
                    Text(
                        color = Color.Gray,
                        text = stringResource(R.string.search),
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = TextUnit(16f, TextUnitType.Sp)
                    )
                },
            )

            // Filter button
            Icon(
                painterResource(R.drawable.ic_filter),
                tint = Primary,
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.largeSection)
                    .padding(start = Dimens.lg)
                    .clickable {
                        viewModel.onFilterClicked()
                    }
            )
        }

        /**
         * Handle Paging LoadState.
         */
        when (movieData.loadState.refresh) {

            // Initial loading state
            is LoadState.Loading -> ProgressIndicator()

            // Error state
            is LoadState.Error -> NoData()

            // Successful load
            is LoadState.NotLoading -> {

                when {

                    // Show list if items exist
                    movieData.itemCount != 0 ->
                        LazyColumn(
                            modifier = Modifier.padding(top = Dimens.inset)
                        ) {
                            items(movieData.itemCount) { index ->
                                movieData[index]?.let { movie ->
                                    MovieItem(
                                        movie = movie,
                                        onItemClick = {
                                            navController.navigate(
                                                "movie_detail/${movie.id}"
                                            )
                                        },
                                        onFavUpdate = {
                                            viewModel.toggleFavourite(movie)
                                        },
                                    )
                                }
                            }
                        }

                    // Show empty state if no results
                    else -> NoData()
                }
            }
        }
    }
}

/**
 * Represents a single movie item inside the list.
 *
 * Displays:
 * - Poster
 * - Title
 * - Release date
 * - Rating
 * - Favourite toggle
 */
@SuppressLint("DefaultLocale")
@Composable
fun MovieItem(
    movie: Movie,
    onItemClick: (Int) -> Unit,
    onFavUpdate: (Movie) -> Unit
) {

    // Construct full poster URL
    val imageUrl = movie.posterPath?.let {
        "${BuildConfig.IMAGE_URL}${EndPoints.POSTER_SIZE_MEDIUM}${it}"
    }


    Card(
        colors = cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(Dimens.lg),
        elevation = CardDefaults.cardElevation(Dimens.hairline),
        modifier = Modifier
            .padding(top = Dimens.lg)
            .clickable {
                onItemClick(movie.id)
            }
    ) {

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.lg)
        ) {

            // Poster image
            SubcomposeAsyncImage(
                model = imageUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = Dimens.stroke
                        )
                    }
                },
                error = {
                    Image(
                        painter = painterResource(R.drawable.ic_default_img),
                        colorFilter = ColorFilter.tint(Color(0xFF1F3A5F)),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .size(Dimens.bannerHeightBig)
                    .clip(RoundedCornerShape(Dimens.lg))
            )

            Column(
                modifier = Modifier.padding(start = Dimens.md)
            ) {

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Start)
                )

                Text(
                    text = stringResource(
                        R.string.release_date,
                        movie.releaseDate.changeDateFormat()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                // Rating row
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        painterResource(R.drawable.ic_star),
                        tint = Accent,
                        contentDescription = null,
                        modifier = Modifier
                            .size(Dimens.xl)
                            .padding(end = Dimens.inset)
                    )

                    Text(
                        text = "${String.format("%.1f", movie.rating).toDouble()}",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                // Favourite toggle row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onFavUpdate(movie)
                    }
                ) {

                    Icon(
                        if (movie.isFavourite)
                            ImageVector.vectorResource(id = R.drawable.ic_heart_filled)
                        else
                            ImageVector.vectorResource(id = R.drawable.ic_heart_outlined),
                        tint = Color.Red,
                        contentDescription = null,
                        modifier = Modifier
                            .size(Dimens.xl)
                            .padding(
                                top = Dimens.inset,
                                end = Dimens.inset
                            )
                    )

                    Text(
                        text =
                            if (movie.isFavourite)
                                stringResource(R.string.removeFav)
                            else
                                stringResource(R.string.addToFav),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = Dimens.inset)
                    )
                }
            }
        }
    }
}
