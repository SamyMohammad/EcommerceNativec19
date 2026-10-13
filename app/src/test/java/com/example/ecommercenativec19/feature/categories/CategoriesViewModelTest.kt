package com.example.ecommercenativec19.feature.categories

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoriesViewModelTest {
    @Test
    fun initialState_selectsMensFashionWithNineSubCategories() {
        val state = CategoriesViewModel().uiState.value
        assertEquals("", state.searchQuery)
        assertEquals(listOf("mens_fashion"), state.railCategories.filter { it.isSelected }.map { it.id })
        assertEquals(9, state.subCategories.size)
    }

    @Test
    fun onlyFootwearOpensProductList() {
        val state = CategoriesViewModel().uiState.value
        assertEquals(listOf("footwear"), state.subCategories.filter { it.opensProductList }.map { it.id })
    }

    @Test
    fun searchQueryChange_updatesState() {
        val viewModel = CategoriesViewModel()
        viewModel.onSearchQueryChange("shirt")
        assertEquals("shirt", viewModel.uiState.value.searchQuery)
        assertTrue(viewModel.uiState.value.subCategories.isNotEmpty())
    }
}
