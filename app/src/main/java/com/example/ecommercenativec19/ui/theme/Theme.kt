package com.example.ecommercenativec19.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// Light only, no dynamic color: the design is one fixed palette. Roles the design never uses keep Material defaults.
private val AppColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    background = White,
    onBackground = InkNavy,
    surface = White,
    onSurface = InkNavy,
    onSurfaceVariant = InkNavy.copy(alpha = 0.6f),
    surfaceVariant = MistBlue,
    surfaceContainer = CloudGray,
    outline = PrimaryBlue.copy(alpha = 0.3f),
    outlineVariant = SlateViolet,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalExtendedColors provides AppExtendedColorValues,
        LocalExtendedTypography provides AppExtendedTypographyValues,
        LocalExtendedShapes provides AppExtendedShapeValues,
    ) {
        MaterialTheme(
            colorScheme = AppColorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}
