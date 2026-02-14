package com.moviebrowser.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moviebrowser.R
import com.moviebrowser.ui.theme.Dimens

/**
 * Reusable composable shown when there is no data to display.
 *
 * Used for:
 * - Empty search results
 * - Empty movie list
 * - Any state where content is unavailable
 */
@Composable
fun NoData() {

    // Center container for empty state UI
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.extraLargeSection),
        contentAlignment = Alignment.Center
    ) {

        // Vertical layout for icon + message
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Icon representing empty content state
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.video),
                contentDescription = stringResource(R.string.nothing_to_display),
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.size(Dimens.largeSection)
            )

            // Informational message
            Text(
                stringResource(R.string.nothing_to_display),
                style = MaterialTheme.typography.displayMedium,
                color = Color.Gray,
                fontSize = Dimens.fontSize22,
                modifier = Modifier.padding(top = Dimens.xxl)
            )
        }
    }
}
