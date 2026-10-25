package com.example.ecommercenativec19.feature.productdetails

import com.example.ecommercenativec19.core.common.UiState

/** What the details screen shows of a loaded product. Prices are whole EGP; the UI formats them. */
data class ProductDetailsUi(
    val id: String,
    val title: String,
    /** What the customer pays for one item. */
    val unitPrice: Int,
    val soldCount: Int,
    val rating: Double,
    val ratingCount: Int,
    val description: String,
    val images: List<String>,
)

/**
 * Everything Product details renders. [quantity] is UI-only until "Add to cart" sends it; [isFavorite] mirrors the shared wishlist cache.
 */
data class ProductDetailsUiState(
    val product: UiState<ProductDetailsUi> = UiState.Loading,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
) {
    /** Unit price times quantity; 0 while there is no product. */
    val totalPrice: Int get() = ((product as? UiState.Success)?.data?.unitPrice ?: 0) * quantity
}
