package com.example.ecommercenativec19.feature.productdetails

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * UI-only state of Product details (favorite, size, color, quantity, carousel page). [initialState] defaults to the
 * design placeholder for [productId]; "Add to cart" has no behavior here beyond the navigation callback.
 */
class ProductDetailsViewModel(
    productId: String,
    initialState: ProductDetailsUiState = ProductDetailsSampleData.forProductId(productId),
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun onToggleFavorite() = _uiState.update { it.copy(isFavorite = !it.isFavorite) }

    fun onSizeSelected(size: String) = _uiState.update { it.copy(selectedSize = size) }

    fun onColorSelected(color: ProductColor) = _uiState.update { it.copy(selectedColor = color) }

    fun onIncrementQuantity() = _uiState.update { it.copy(quantity = it.quantity + 1) }

    /** The design shows no limits; the quantity just never drops below 1 so the pill cannot show 0 or negatives. */
    fun onDecrementQuantity() = _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(MinQuantity)) }

    fun onImagePageChanged(page: Int) = _uiState.update { it.copy(imagePage = page) }

    private companion object {
        const val MinQuantity = 1
    }
}
