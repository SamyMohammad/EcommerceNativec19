package com.example.ecommercenativec19.feature.products.ui

import androidx.annotation.DrawableRes

/** One product card, shared by every screen that lists products. Prices are whole EGP; the UI formats them. */
data class ProductCardUi(
    val id: String,
    @param:DrawableRes val imageRes: Int?,
    val title: String,
    /** What the customer pays. */
    val price: Int,
    /** The undiscounted price, set only for discounted products. */
    val oldPrice: Int?,
    val rating: Double,
)
