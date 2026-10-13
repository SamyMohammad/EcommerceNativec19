package com.example.ecommercenativec19.feature.products.domain

/** Prices are whole EGP, as the API sends them. */
data class Product(
    val id: String,
    val title: String,
    val description: String,
    /** The undiscounted price. */
    val price: Int,
    /** Set only when it is really below [price]. */
    val priceAfterDiscount: Int?,
    /** Cover photo; null when the product has no image at all. */
    val imageUrl: String?,
    val images: List<String>,
    val rating: Double,
    val ratingCount: Int,
    val soldCount: Int,
    val categoryId: String?,
    val brandName: String?,
) {
    /** What the customer pays. */
    val currentPrice: Int get() = priceAfterDiscount ?: price
}

/** One page of a product list. [nextPage] is null on the last page. */
data class ProductPage(
    val items: List<Product>,
    val nextPage: Int?,
)

/** The orderings the app asks the API for. Each one is a ranking, so the first items are the "best" ones. */
enum class ProductSort { BestSelling, Newest, TopRated }
