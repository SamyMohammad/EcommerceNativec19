package com.example.ecommercenativec19.feature.productlist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductListViewModelTest {
    private val items = listOf(
        ProductListItem("a", 1, "A", "EGP 1", null, "4.0"),
        ProductListItem("b", 2, "B", "EGP 2", "EGP 3", "4.5"),
    )

    @Test
    fun startsWithGivenProductsAndEmptyQuery() {
        val state = ProductListViewModel(items).uiState.value
        assertEquals(items, state.products)
        assertEquals("", state.query)
    }

    @Test
    fun toggleFavoriteFlipsOnlyThatProduct() {
        val viewModel = ProductListViewModel(items)
        viewModel.onToggleFavorite("b")
        assertTrue(viewModel.uiState.value.products.first { it.id == "b" }.isFavorite)
        assertFalse(viewModel.uiState.value.products.first { it.id == "a" }.isFavorite)
        viewModel.onToggleFavorite("b")
        assertFalse(viewModel.uiState.value.products.first { it.id == "b" }.isFavorite)
    }

    @Test
    fun queryChangeIsStoredWithoutFilteringProducts() {
        val viewModel = ProductListViewModel(items)
        viewModel.onQueryChange("nike")
        assertEquals("nike", viewModel.uiState.value.query)
        assertEquals(items, viewModel.uiState.value.products)
    }
}
