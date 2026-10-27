package com.example.ecommercenativec19.feature.cart

import com.example.ecommercenativec19.core.common.UiState

/** One cart row. Prices are whole EGP; the UI formats them. */
data class CartLineUi(
    val id: String,
    val title: String,
    val imageUrl: String?,
    /** Unit price times [quantity]. */
    val linePrice: Int,
    val quantity: Int,
)

/**
 * [items] follows the shared cart cache: counts change the moment the user taps and settle on the server's answer.
 * [totalPrice] is the server's total (0 while there is nothing to show).
 */
data class CartUiState(
    val items: UiState<List<CartLineUi>> = UiState.Loading,
    val totalPrice: Int = 0,
)
