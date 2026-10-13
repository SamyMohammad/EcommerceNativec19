package com.example.ecommercenativec19.feature.products.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wire shape shared by the list and details endpoints. Everything is nullable or defaulted because the mapper, not the
 * decoder, decides what a usable product is: one malformed item must not fail the whole page.
 */
@Serializable
data class ProductDto(
    @SerialName("_id") val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    /** Whole EGP. */
    val price: Int? = null,
    /** Present only when the product is discounted. */
    val priceAfterDiscount: Int? = null,
    val imageCover: String? = null,
    val images: List<String>? = null,
    val ratingsAverage: Double? = null,
    val ratingsQuantity: Int? = null,
    val sold: Int? = null,
    val category: ProductCategoryDto? = null,
    val brand: ProductBrandDto? = null,
)

@Serializable
data class ProductCategoryDto(
    @SerialName("_id") val id: String? = null,
)

@Serializable
data class ProductBrandDto(
    val name: String? = null,
)
