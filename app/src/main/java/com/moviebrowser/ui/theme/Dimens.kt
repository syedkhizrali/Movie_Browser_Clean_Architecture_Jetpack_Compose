package com.moviebrowser.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/**
 * Centralized dimension definitions used throughout the app.
 *
 * Contains:
 * - Spacing values (padding, margins)
 * - Component sizes (icons, banners, posters)
 * - Corner radius values
 * - Font sizes
 *
 * This ensures consistent UI design and avoids hardcoded
 * dimension values across composables.
 */

object Dimens {

    // Tiny elements
    val hairline = 1.dp
    val stroke = 2.dp
    val inset = 5.dp
    val offset = (-27).dp


    // Spacing and paddings
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
    val xxxl = 32.dp

    // Large layout spacing
    val section = 40.dp
    val largeSection = 50.dp
    val extraLargeSection = 75.dp

    // Component sizes
    val iconSmall = 20.dp
    val iconMedium = 24.dp
    val iconMedium27 = 27.dp
    val iconLarge = 40.dp
    val buttonHeight = 50.dp
    val bannerHeight = 80.dp
    val bannerHeightBig = 90.dp
    val posterHeight = 350.dp

    // Corners
    val radiusSmall = 12.dp
    val radiusMedium = 16.dp
    val radiusLarge = 24.dp
    val radiusXLarge = 30.dp



    //Text Dimens
    val fontSize14 = 14.sp
    val fontSize24 = 24.sp
    val fontSizeDot5 = 0.5.sp
    val fontSize20 = 20.sp
    val fontSize28 = 28.sp
    val fontSize0 = 0.sp
    val fontSize10 = 10.sp
    val fontSize16 = 16.sp
    val fontSize22 =22.sp
}
