package com.example.ecommercenativec19.feature.cart.domain

/** One product in the cart. Prices are whole EGP. */
data class CartLine(
    val productId: String,
    val title: String,
    val imageUrl: String?,
    /** The price the server charges for one item; it already includes any discount. */
    val unitPrice: Int,
    val count: Int,
) {
    val linePrice: Int get() = unitPrice * count
}

/**
 * The signed-in user's cart. [totalPrice] is the server's figure; the app only computes it itself for the short time
 * between an optimistic count change and the server's answer.
 */
data class Cart(
    val items: List<CartLine>,
    val totalPrice: Int,
) {
    /** Units, not lines: three shawls are 3. (The API's own `numOfCartItems` counts lines.) */
    val itemCount: Int get() = items.sumOf { it.count }
}
