package com.example.ecommercenativec19.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

// What Material's roles have no honest name for. Read through MaterialTheme.extendedColors / extendedTypography /
// extendedShapes so call sites have one entry point; static locals because the values never change at runtime.

@Immutable
data class AppExtendedColors(
    val splashGlow: Color,
    val inputText: Color,
    val inputPlaceholder: Color,
    val footwearBanner: Color,
    val oldPrice: Color,
    val searchFieldContainer: Color,
    val swatchBlack: Color,
    val swatchRed: Color,
    val swatchBlue: Color,
    val swatchGreen: Color,
    val swatchCoral: Color,
    val variantRust: Color,
    val variantCharcoal: Color,
    val variantAmber: Color,
    val variantPeach: Color,
)

@Immutable
data class AppExtendedTypography(
    val authButton: TextStyle,
    val fieldInput: TextStyle,
    val inlineLink: TextStyle,
    val bannerOffer: TextStyle,
    val searchInput: TextStyle,
)

@Immutable
data class AppExtendedShapes(
    val searchField: CornerBasedShape,
)

internal val AppExtendedColorValues = AppExtendedColors(
    splashGlow = White.copy(alpha = 0.4f),
    inputText = Black,
    inputPlaceholder = Black.copy(alpha = 0.7f),
    footwearBanner = SkyBlue,
    oldPrice = PrimaryBlue.copy(alpha = 0.6f),
    searchFieldContainer = White.copy(alpha = 0.7f),
    swatchBlack = SwatchBlack,
    swatchRed = SwatchRed,
    swatchBlue = SwatchBlue,
    swatchGreen = SwatchGreen,
    swatchCoral = SwatchCoral,
    variantRust = DotRust,
    variantCharcoal = DotCharcoal,
    variantAmber = DotAmber,
    variantPeach = DotPeach,
)

internal val LocalExtendedColors = staticCompositionLocalOf<AppExtendedColors> { error("AppTheme is required") }
internal val LocalExtendedTypography = staticCompositionLocalOf<AppExtendedTypography> { error("AppTheme is required") }
internal val LocalExtendedShapes = staticCompositionLocalOf<AppExtendedShapes> { error("AppTheme is required") }

val MaterialTheme.extendedColors: AppExtendedColors
    @Composable @ReadOnlyComposable get() = LocalExtendedColors.current

val MaterialTheme.extendedTypography: AppExtendedTypography
    @Composable @ReadOnlyComposable get() = LocalExtendedTypography.current

val MaterialTheme.extendedShapes: AppExtendedShapes
    @Composable @ReadOnlyComposable get() = LocalExtendedShapes.current
