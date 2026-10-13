package com.example.ecommercenativec19.feature.products.ui

import com.example.ecommercenativec19.feature.products.domain.Product

/** One product card, shared by every screen that lists products. Prices are whole EGP; the UI formats them. */
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

fun Product.toCardUi() = ProductCardUi(
    id = id,
    imageUrl = imageUrl,
    title = title,
    price = currentPrice,
    oldPrice = if (priceAfterDiscount != null) price else null,
    rating = rating,
)
