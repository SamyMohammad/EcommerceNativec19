package com.example.ecommercenativec19.feature.productlist

import com.example.ecommercenativec19.core.common.LoadState

/** One card of the product list. Prices are whole EGP; the UI formats them. */
data class ProductCardUi(
    val id: String,
    val imageUrl: String?,
    val title: String,
    /** What the customer pays. */
    val price: Int,
    /** The undiscounted price, set only for discounted products. */
    val oldPrice: Int?,
    val rating: Double,
)

/** State of the "load more" footer below an already visible list. */
enum class LoadMoreState { Idle, Loading, Error }

data class ProductListUiState(
    val query: String = "",
    val products: LoadState<List<ProductCardUi>> = LoadState.Loading,
    /** Local only until the wishlist API slice; kept apart from [products] so reloads do not wipe the hearts. */
    val favoriteIds: Set<String> = emptySet(),
    val loadMore: LoadMoreState = LoadMoreState.Idle,
)
