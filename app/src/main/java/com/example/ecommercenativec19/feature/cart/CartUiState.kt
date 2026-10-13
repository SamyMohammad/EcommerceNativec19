package com.example.ecommercenativec19.feature.cart

import androidx.annotation.DrawableRes
import com.example.ecommercenativec19.core.ui.ProductVariantDot

/** One cart row. Text arrives formatted; there is no price/quantity rule in the design. */
data class CartItemUi(
    val id: String,
    val title: String,
    val price: String,
    @param:DrawableRes val image: Int,
    val dot: ProductVariantDot,
    val variantLabel: String,
    val variantDetail: String,
    val quantity: Int,
)

/** [totalPrice] is held as given (placeholder); it is NOT recomputed when quantities change or rows are removed. */
data class CartUiState(
    val items: List<CartItemUi> = emptyList(),
    val totalPrice: String = "",
)
