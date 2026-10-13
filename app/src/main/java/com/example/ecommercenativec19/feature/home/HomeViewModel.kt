package com.example.ecommercenativec19.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** UI-only state of Home: search text and favorite toggles. No search / persistence contract exists in the design. */
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            categories = HomeSampleData.categories,
            applianceProducts = HomeSampleData.applianceProducts,
            newArrivalProducts = HomeSampleData.newArrivalProducts,
            smartWatchProducts = HomeSampleData.smartWatchProducts,
        ),
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onFavoriteToggle(productId: String) {
        _uiState.update { state ->
            val favorites =
                if (productId in state.favoriteIds) state.favoriteIds - productId else state.favoriteIds + productId
            state.copy(favoriteIds = favorites)
        }
    }
}
