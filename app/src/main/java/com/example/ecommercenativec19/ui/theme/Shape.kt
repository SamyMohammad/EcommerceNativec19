package com.example.ecommercenativec19.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

internal val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(5.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(15.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(30.dp),
)

internal val AppExtendedShapeValues = AppExtendedShapes(
    searchField = RoundedCornerShape(25.dp),
)
