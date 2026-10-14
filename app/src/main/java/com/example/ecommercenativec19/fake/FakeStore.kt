package com.example.ecommercenativec19.fake

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.ecommercenativec19.feature.account.Profile

/** One cart row: the product and how many units. */
data class CartEntry(val productId: String, val quantity: Int)

/**
 * The app's only state holder: favorites, cart, profile and the signed-in flag, all in memory. Every property is
 * Compose snapshot state, so screens that read it recompose when it changes. Nothing is persisted; a new process
 * starts again from the seed below.
 */
class FakeStore {
    var signedIn by mutableStateOf(false)
    var profile by mutableStateOf(FakeData.profile)
        private set

    val favoriteIds = mutableStateListOf("product-1", "product-2", "product-3")
    val cart = mutableStateListOf(CartEntry("product-9", 1))

    fun toggleFavorite(productId: String) {
        if (!favoriteIds.remove(productId)) favoriteIds.add(productId)
    }

    fun addToCart(productId: String, quantity: Int = 1) {
        val index = cart.indexOfFirst { it.productId == productId }
        if (index < 0) cart.add(CartEntry(productId, quantity)) else cart[index] = cart[index].copy(quantity = cart[index].quantity + quantity)
    }

    /** Going below 1 removes the line, so the quantity pill never shows 0. */
    fun changeQuantity(productId: String, delta: Int) {
        val index = cart.indexOfFirst { it.productId == productId }
        if (index < 0) return
        val quantity = cart[index].quantity + delta
        if (quantity < 1) cart.removeAt(index) else cart[index] = cart[index].copy(quantity = quantity)
    }

    fun removeFromCart(productId: String) {
        cart.removeAll { it.productId == productId }
    }

    fun updateProfile(profile: Profile) {
        this.profile = profile
    }

    fun signIn() {
        signedIn = true
    }

    fun signOut() {
        signedIn = false
    }
}

/** Provided once in MainActivity; routes read it instead of receiving it through every navigation callback. */
val LocalFakeStore = staticCompositionLocalOf<FakeStore> { error("FakeStore is not provided") }
