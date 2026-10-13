package com.example.ecommercenativec19.feature.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {
    @Test
    fun initialState_hasPlaceholderContentAndNoQuery() {
        val state = HomeViewModel().uiState.value
        assertEquals("", state.searchQuery)
        assertEquals(8, state.categories.size)
        assertEquals(3, state.applianceProducts.size)
        assertTrue(state.favoriteIds.isEmpty())
    }

    @Test
    fun searchQueryChange_updatesState() {
        val viewModel = HomeViewModel()
        viewModel.onSearchQueryChange("watch")
        assertEquals("watch", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun favoriteToggle_addsThenRemovesProduct() {
        val viewModel = HomeViewModel()
        viewModel.onFavoriteToggle("washing_machine")
        assertTrue("washing_machine" in viewModel.uiState.value.favoriteIds)
        viewModel.onFavoriteToggle("washing_machine")
        assertFalse("washing_machine" in viewModel.uiState.value.favoriteIds)
    }
}
