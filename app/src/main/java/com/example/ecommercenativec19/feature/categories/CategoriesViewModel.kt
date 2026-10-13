package com.example.ecommercenativec19.feature.categories

import androidx.lifecycle.ViewModel
import com.example.ecommercenativec19.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** UI-only state of Categories: the search text. There is no search / category-data contract in the design. */
class CategoriesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CategoriesUiState(
            railCategories = CategoriesSampleData.rail,
            selectedTitle = R.string.categories_rail_mens_fashion,
            subCategories = CategoriesSampleData.subCategories,
        ),
    )
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
