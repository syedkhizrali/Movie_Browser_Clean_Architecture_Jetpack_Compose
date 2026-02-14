package com.moviebrowser.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moviebrowser.R
/**
 * Defines the application's typography system.
 *
 * Contains:
 * - Custom font family (Roboto)
 * - Text styles mapped to Material3 Typography
 * - Centralized font sizes and spacing via Dimens
 *
 * Ensures consistent text styling across the UI
 * and avoids hardcoded font configurations.
 */


val roboto = FontFamily(
    Font(R.font.roboto_bold),
    Font(R.font.roboto_semibold),
    Font(R.font.roboto_light),
    Font(R.font.roboto_regular),
    Font(R.font.roboto_medium)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = Dimens.fontSize14,
        lineHeight = Dimens.fontSize24,
        letterSpacing = Dimens.fontSizeDot5
    ),
    titleLarge = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
        fontSize = Dimens.fontSize20,
        lineHeight = Dimens.fontSize28,
        letterSpacing = Dimens.fontSize0
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = Dimens.fontSize10,
        lineHeight = Dimens.fontSize16,
        letterSpacing = Dimens.fontSizeDot5
    )
)