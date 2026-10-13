package com.example.ecommercenativec19.feature.productlist

import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

/** State of the "load more" footer below an already visible list. */
enum class LoadMoreState { Idle, Loading, Error }

data class ProductListUiState(
    val query: String = "",
    val products: LoadState<List<ProductCardUi>> = LoadState.Loading,
    /** Local only until the wishlist API slice; kept apart from [products] so reloads do not wipe the hearts. */
    val favoriteIds: Set<String> = emptySet(),
    val loadMore: LoadMoreState = LoadMoreState.Idle,
)
