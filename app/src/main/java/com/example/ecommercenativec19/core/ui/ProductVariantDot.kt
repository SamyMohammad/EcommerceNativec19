package com.example.ecommercenativec19.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.ecommercenativec19.ui.theme.extendedColors

/**
 * Color dots of the product rows (Cart, Wishlist). The design draws them as separate ellipses; their fills are
 * extended theme colors (`variantRust` / `variantCharcoal` / `variantAmber` / `variantPeach`), so a screen state holds this enum and [tokenColor] resolves it
 * for `ProductRowCard(colorDot = ...)`.
 */
enum class ProductVariantDot { Rust, Charcoal, Amber, Peach }

@Composable
@ReadOnlyComposable
fun ProductVariantDot.tokenColor(): Color {
    return when (this) {
        ProductVariantDot.Rust -> MaterialTheme.extendedColors.variantRust
        ProductVariantDot.Charcoal -> MaterialTheme.extendedColors.variantCharcoal
        ProductVariantDot.Amber -> MaterialTheme.extendedColors.variantAmber
        ProductVariantDot.Peach -> MaterialTheme.extendedColors.variantPeach
    }
}
