package com.example.ecommercenativec19.feature.cart

import com.example.ecommercenativec19.core.ui.ProductVariantDot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartViewModelTest {
    private fun vm() = CartViewModel(
        CartUiState(
            items = listOf(CartItemUi("a", "A", "EGP 1", 0, ProductVariantDot.Rust, "Orange", "Size: 40", 1)),
            totalPrice = "EGP 1",
        ),
    )

    @Test fun incrementAndDecrementChangeQuantityButNotBelowOne() {
        val vm = vm()
        vm.onIncrement("a")
        assertEquals(2, vm.state.value.items.single().quantity)
        vm.onDecrement("a")
        vm.onDecrement("a")
        assertEquals(1, vm.state.value.items.single().quantity)
    }

    @Test fun removeDropsItemAndKeepsPlaceholderTotal() {
        val vm = vm()
        vm.onRemove("a")
        assertTrue(vm.state.value.items.isEmpty())
        assertEquals("EGP 1", vm.state.value.totalPrice)
    }
}
