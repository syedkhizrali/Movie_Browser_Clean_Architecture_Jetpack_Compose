package com.moviebrowser.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Application-level theme composable.
 *
 * Responsible for:
 * - Providing color scheme
 * - Providing typography
 * - Wrapping entire UI with MaterialTheme
 *
 * All composables inside this theme will inherit:
 * - Colors
 * - Typography styles
 * - Shape configurations (if added)
 */
@Composable
fun MovieBrowserTheme(
    content: @Composable () -> Unit
) {

    /**
     * Defines the color scheme used by the app.
     *
     * Currently using a light color scheme
     * with Primary as the main brand color.
     */
    val colorScheme = lightColorScheme(Primary)

    /**
     * MaterialTheme provides:
     * - colorScheme
     * - typography
     * - shapes (if configured)
     *
     * This ensures consistent styling across the app.
     */
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
