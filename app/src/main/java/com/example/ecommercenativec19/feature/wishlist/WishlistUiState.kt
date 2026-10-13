package com.example.ecommercenativec19.feature.wishlist

import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

/**
 * [items] only ever holds products that are still favorites: un-hearting a row removes it at once and a failed
 * request brings it back, because the list is derived from the shared favorite ids (see [WishlistViewModel]).
 */
data class WishlistUiState(
    val query: String = "",
    val items: LoadState<List<ProductCardUi>> = LoadState.Loading,
)
