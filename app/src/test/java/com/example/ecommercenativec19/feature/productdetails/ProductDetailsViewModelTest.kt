package com.example.ecommercenativec19.feature.productdetails

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductDetailsViewModelTest {
    private fun viewModel() = ProductDetailsViewModel("product-1")

    @Test
    fun startsFromPlaceholderForTheGivenProductId() {
        val state = viewModel().uiState.value
        assertEquals("product-1", state.productId)
        assertEquals(1, state.quantity)
        assertEquals("40", state.selectedSize)
        assertEquals(ProductColor.Red, state.selectedColor)
        assertFalse(state.isFavorite)
    }

    @Test
    fun quantityIncrementsAndNeverDropsBelowOne() {
        val vm = viewModel()
        vm.onIncrementQuantity()
        assertEquals(2, vm.uiState.value.quantity)
        vm.onDecrementQuantity()
        vm.onDecrementQuantity()
        assertEquals(1, vm.uiState.value.quantity)
    }

    @Test
    fun selectionsFavoriteAndPageAreStored() {
        val vm = viewModel()
        vm.onSizeSelected("38")
        vm.onColorSelected(ProductColor.Blue)
        vm.onToggleFavorite()
        vm.onImagePageChanged(2)
        val state = vm.uiState.value
        assertEquals("38", state.selectedSize)
        assertEquals(ProductColor.Blue, state.selectedColor)
        assertTrue(state.isFavorite)
        assertEquals(2, state.imagePage)
    }
}
