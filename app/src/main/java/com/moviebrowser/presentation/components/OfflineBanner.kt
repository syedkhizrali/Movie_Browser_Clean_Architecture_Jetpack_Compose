package com.moviebrowser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moviebrowser.R
import com.moviebrowser.ui.theme.Dimens

/**
 * Banner displayed when the device is offline.
 *
 * Intended to be shown conditionally based on
 * network connectivity state.
 */
@Preview
@Composable
fun OfflineBanner() {

    // Full-width container with red background to indicate error/offline state
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .padding(Dimens.sm),
        contentAlignment = Alignment.Center
    ) {

        // Informational message shown to user
        Text(
            text = stringResource(R.string.you_re_offline),
            color = Color.White
        )
    }
}
