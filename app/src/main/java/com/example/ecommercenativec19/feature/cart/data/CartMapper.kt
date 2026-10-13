package com.example.ecommercenativec19.feature.cart.data

import com.example.ecommercenativec19.feature.cart.domain.Cart
import com.example.ecommercenativec19.feature.cart.domain.CartLine

/**
 * Lines the app cannot show (no title, count or price: for example a POST answer, whose products are bare ids) are
 * dropped, but [Cart.totalPrice] stays the server's number.
 */
internal fun CartResponse.toDomain(): Cart {
    val lines = data.products.mapNotNull { it.toDomain() }.distinctBy { it.productId }
    return Cart(items = lines, totalPrice = data.totalCartPrice ?: lines.sumOf { it.linePrice })
}

private fun CartLineDto.toDomain(): CartLine? {
    val product = product ?: return null
    return CartLine(
        productId = product.id?.takeIf { it.isNotBlank() } ?: return null,
        title = product.title?.trim()?.takeIf { it.isNotEmpty() } ?: return null,
        imageUrl = product.imageCover?.takeIf { it.isNotBlank() },
        unitPrice = price?.takeIf { it >= 0 } ?: return null,
        count = count?.takeIf { it >= 1 } ?: return null,
    )
}
