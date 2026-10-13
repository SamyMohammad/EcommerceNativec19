package com.example.ecommercenativec19.feature.wishlist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WishlistViewModelTest {
    @Test fun toggleFavoriteFlipsOnlyThatItem() {
        val vm = WishlistViewModel()
        val first = vm.state.value.items.first().id
        vm.onToggleFavorite(first)
        assertFalse(vm.state.value.items.first().isFavorite)
        assertTrue(vm.state.value.items[1].isFavorite)
        vm.onToggleFavorite(first)
        assertTrue(vm.state.value.items.first().isFavorite)
    }

    @Test fun queryChangeIsStored() {
        val vm = WishlistViewModel()
        vm.onQueryChange("shoe")
        assertEquals("shoe", vm.state.value.query)
    }
}
