package com.moviebrowser.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviebrowser.ui.theme.Dimens

/**
 * Reusable loading indicator component.
 *
 * Used to indicate ongoing background operations such as:
 * - Initial data loading
 * - Pagination loading
 * - Refresh states
 */
@Composable
fun ProgressIndicator() {

    // Centers the progress indicator horizontally
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.extraLargeSection),
        contentAlignment = Alignment.Center
    ) {

        // Material circular loading indicator
        CircularProgressIndicator()
    }
}
