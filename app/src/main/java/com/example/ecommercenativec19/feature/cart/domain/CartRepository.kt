package com.example.ecommercenativec19.feature.cart.domain

import com.example.ecommercenativec19.core.common.AppResult
import kotlinx.coroutines.flow.StateFlow

/**
 * The single source of truth for the cart, so a product added on Details is already counted when Cart opens.
 *
 * Every change ends with the server's cart replacing the cache, so totals are never the app's own arithmetic.
 */
interface CartRepository {
    /** The signed-in user's cart; null while it has not been loaded and when signed out. */
    val cart: StateFlow<Cart?>

    /** Fetches the cart and replaces [cart] with it. */
    suspend fun refresh(): AppResult<Unit>

    /**
     * Puts [quantity] more units of [productId] in the cart. Not optimistic: the new line has no title or price until
     * the server answers.
     */
    suspend fun addProduct(productId: String, quantity: Int = 1): AppResult<Unit>

    /**
     * Sets the count of a product that is in the cart; [count] must be at least 1 (use [remove] for 0). [cart] shows
     * the new count at once. Taps on the same product while a request is on the wire are coalesced: only the latest
     * count is sent next, and those calls report success. A failure restores the last count the server confirmed.
     */
    suspend fun updateCount(productId: String, count: Int): AppResult<Unit>

    /** Removes a product from the cart, optimistically, with the same coalescing and rollback as [updateCount]. */
    suspend fun remove(productId: String): AppResult<Unit>
}
