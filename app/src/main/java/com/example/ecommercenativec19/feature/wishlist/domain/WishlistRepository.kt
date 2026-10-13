package com.example.ecommercenativec19.feature.wishlist.domain

import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.feature.products.domain.Product
import kotlinx.coroutines.flow.StateFlow

/**
 * The single source of truth for "is this product a favorite". Every screen with a heart reads [favoriteIds] instead
 * of keeping its own copy, so a heart tapped on Details is already filled on Home.
 */
interface WishlistRepository {
    /** In-memory cache of the signed-in user's favorite product ids; empty when signed out. */
    val favoriteIds: StateFlow<Set<String>>

    /** Fetches the full wishlist for the Wishlist screen and re-syncs [favoriteIds] with it. */
    suspend fun refresh(): AppResult<List<Product>>

    /**
     * Flips the favorite flag of [productId]. [favoriteIds] changes immediately (optimistic) and is rolled back if the
     * request fails. A call for a product whose previous toggle is still in flight is ignored and reports success.
     */
    suspend fun toggle(productId: String): AppResult<Unit>
}
