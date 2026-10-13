package com.example.ecommercenativec19.feature.productlist

import androidx.annotation.DrawableRes

/** One card of the product list. Texts are already formatted for display (no price math happens in the UI). */
data class ProductListItem(
    val id: String,
    @param:DrawableRes val imageRes: Int,
    val title: String,
    val price: String,
    val oldPrice: String?,
    val rating: String,
    val isFavorite: Boolean = false,
)

data class ProductListUiState(
    val query: String = "",
    val products: List<ProductListItem> = emptyList(),
)
