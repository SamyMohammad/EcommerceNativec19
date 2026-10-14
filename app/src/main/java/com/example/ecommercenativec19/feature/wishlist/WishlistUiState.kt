package com.example.ecommercenativec19.feature.wishlist

import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

/**
 * [items] only ever holds products that are still favorites: un-hearting a row removes it at once, because the
 * list is derived from the in-memory favorite ids.
 */
data class WishlistUiState(
    val query: String = "",
    val items: LoadState<List<ProductCardUi>> = LoadState.Loading,
)
