package com.example.ecommercenativec19.feature.cart

import androidx.annotation.DrawableRes
import com.example.ecommercenativec19.core.common.LoadState

/** One cart row. Prices are whole EGP; the UI formats them. */
data class CartLineUi(
    val id: String,
    val title: String,
    @param:DrawableRes val imageRes: Int?,
    /** Unit price times [quantity]. */
    val linePrice: Int,
    val quantity: Int,
)

/**
 * [items] follows the shared cart cache: counts change the moment the user taps and settle on the server's answer.
 * [totalPrice] is the server's total (0 while there is nothing to show).
 */
data class CartUiState(
    val items: LoadState<List<CartLineUi>> = LoadState.Loading,
    val totalPrice: Int = 0,
)
