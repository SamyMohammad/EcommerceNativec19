package com.example.ecommercenativec19.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Color dots of the product rows (Cart, Wishlist). The design draws them as separate ellipses; their fills are
 * theme tokens (colorSource29 / 31 / 32 / 33), so a screen state holds this enum and [tokenColor] resolves it
 * for `ProductRowCard(colorDot = ...)`.
 */
enum class ProductVariantDot { Rust, Charcoal, Amber, Peach }

@Composable
@ReadOnlyComposable
fun ProductVariantDot.tokenColor(): Color {
    val tokens = LocalAppTokens.current
    return when (this) {
        ProductVariantDot.Rust -> tokens.colorSource29
        ProductVariantDot.Charcoal -> tokens.colorSource31
        ProductVariantDot.Amber -> tokens.colorSource32
        ProductVariantDot.Peach -> tokens.colorSource33
    }
}
