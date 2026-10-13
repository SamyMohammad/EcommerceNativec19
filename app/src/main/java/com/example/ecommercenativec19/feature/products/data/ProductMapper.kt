package com.example.ecommercenativec19.feature.products.data

import com.example.ecommercenativec19.feature.products.domain.Product

/**
 * Returns null for a product the app cannot show (no id, title or valid price) so callers can drop it instead of
 * rendering a broken card. Everything else is defaulted.
 */
internal fun ProductDto.toDomain(): Product? {
    val productId = id?.takeIf { it.isNotBlank() } ?: return null
    val productTitle = title?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    val originalPrice = price?.takeIf { it >= 0 } ?: return null
    val gallery = images.orEmpty().filter { it.isNotBlank() }
    val cover = imageCover?.takeIf { it.isNotBlank() } ?: gallery.firstOrNull()
    return Product(
        id = productId,
        title = productTitle,
        description = description.orEmpty().trim(),
        price = originalPrice,
        // A "discount" that is not below the price would show a nonsensical struck-through price.
        priceAfterDiscount = priceAfterDiscount?.takeIf { it in 0 until originalPrice },
        imageUrl = cover,
        images = gallery.ifEmpty { listOfNotNull(cover) },
        rating = (ratingsAverage ?: 0.0).coerceIn(0.0, MAX_RATING),
        ratingCount = (ratingsQuantity ?: 0).coerceAtLeast(0),
        soldCount = (sold ?: 0).coerceAtLeast(0),
        categoryId = category?.id?.takeIf { it.isNotBlank() },
        brandName = brand?.name?.takeIf { it.isNotBlank() },
    )
}

private const val MAX_RATING = 5.0
