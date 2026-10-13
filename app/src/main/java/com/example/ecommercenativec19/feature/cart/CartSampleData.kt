package com.example.ecommercenativec19.feature.cart

import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.ProductVariantDot

/**
 * DESIGN PLACEHOLDER content copied from the Figma Cart frame (node 604:890). There is no cart data contract yet;
 * replace with real data when one exists.
 */
internal object CartSampleData {
    val items = listOf(
        CartItemUi(
            id = "nike-air-jordon",
            title = "Nike Air Jordon",
            price = "EGP 3,500",
            image = R.drawable.img_frame69_80e90f22,
            dot = ProductVariantDot.Rust,
            variantLabel = "Orange",
            variantDetail = "Size: 40",
            quantity = 1,
        ),
    )
    const val TOTAL_PRICE = "EGP 3,500"
}
