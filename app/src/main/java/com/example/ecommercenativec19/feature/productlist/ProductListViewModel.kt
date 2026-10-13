package com.example.ecommercenativec19.feature.productlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** UI-only state of Product list: the search text and the favorite hearts. No filtering, no persistence. */
class ProductListViewModel(
    initialProducts: List<ProductListItem> = ProductListSampleData.items,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductListUiState(products = initialProducts))
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onToggleFavorite(productId: String) {
        _uiState.update { state ->
            state.copy(
                products = state.products.map { if (it.id == productId) it.copy(isFavorite = !it.isFavorite) else it },
            )
        }
    }
}
