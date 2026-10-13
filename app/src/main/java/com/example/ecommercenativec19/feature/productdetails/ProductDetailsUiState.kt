package com.example.ecommercenativec19.feature.productdetails

/** The five color swatches of the design; the actual colors come from the extended theme colors in the UI layer. */
enum class ProductColor { Black, Red, Blue, Green, Coral }

/**
 * Everything Product details renders. Display texts are already formatted (no price math in the UI); the
 * selection fields ([selectedSize], [selectedColor], [quantity], [isFavorite]) are UI-only state.
 */
data class ProductDetailsUiState(
    val productId: String,
    val title: String,
    val price: String,
    val soldLabel: String,
    val ratingLabel: String,
    val description: String,
    /** Drawable resource ids of the carousel pages. */
    val images: List<Int>,
    val sizes: List<String>,
    val colors: List<ProductColor>,
    val totalPrice: String,
    val selectedSize: String,
    val selectedColor: ProductColor,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
)
