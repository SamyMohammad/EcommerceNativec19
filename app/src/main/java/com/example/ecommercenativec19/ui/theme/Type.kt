package com.example.ecommercenativec19.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.ecommercenativec19.R

private val Poppins = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

// The design uses an 18sp line box and -0.165sp tracking for every style except two taller line boxes.
private fun poppins(weight: FontWeight, size: Int, lineHeight: TextUnit = 18.sp) = TextStyle(
    fontFamily = Poppins,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight,
    letterSpacing = (-0.165).sp,
)

// Slots the design never uses (display*, labelMedium) keep the Material defaults.
internal val AppTypography = Typography(
    headlineLarge = poppins(FontWeight.Medium, 30),
    headlineMedium = poppins(FontWeight.SemiBold, 24),
    headlineSmall = poppins(FontWeight.Medium, 22),
    titleLarge = poppins(FontWeight.Medium, 20),
    titleMedium = poppins(FontWeight.Medium, 18),
    titleSmall = poppins(FontWeight.SemiBold, 16, lineHeight = 20.sp),
    bodyLarge = poppins(FontWeight.Light, 16),
    bodyMedium = poppins(FontWeight.Normal, 14),
    bodySmall = poppins(FontWeight.Normal, 12),
    labelLarge = poppins(FontWeight.Medium, 14),
    labelSmall = poppins(FontWeight.Normal, 11),
)

internal val AppExtendedTypographyValues = AppExtendedTypography(
    authButton = poppins(FontWeight.SemiBold, 20),
    fieldInput = poppins(FontWeight.Light, 18),
    inlineLink = poppins(FontWeight.Normal, 18),
    bannerOffer = poppins(FontWeight.Medium, 20, lineHeight = 22.sp),
    searchInput = poppins(FontWeight.Light, 14),
)
