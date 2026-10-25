package com.example.ecommercenativec19.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.categories.domain.GetCategoriesUseCase
import com.example.ecommercenativec19.feature.products.domain.GetProductsUseCase
import com.example.ecommercenativec19.feature.products.domain.ProductSort
import com.example.ecommercenativec19.feature.products.ui.toCardUi
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Home composes existing use cases: one request for the categories grid plus one per product row. Each region is its
 * own coroutine (so they load in parallel) and has its own [UiState] (so one failure stays local). The search text
 * is UI-only (no search endpoint is used). Hearts mirror the shared wishlist cache; a failed toggle is rolled back
 * there, so the heart simply returns to its previous state. The card "+" adds one unit to the shared cart.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val getProducts: GetProductsUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val addToCart: AddToCartUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var categoriesJob: Job? = null
    private val sectionJobs = mutableMapOf<HomeSection, Job>()

    init {
        viewModelScope.launch {
            observeFavoriteIds().collect { ids -> _uiState.update { it.copy(favoriteIds = ids) } }
        }
        loadCategories()
        HomeSection.entries.forEach(::loadSection)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onFavoriteToggle(productId: String) {
        viewModelScope.launch { toggleFavorite(productId) }
    }

    fun onAddToCart(productId: String) {
        // A failed add leaves the cart as it was; no extra error UI in this slice.
        viewModelScope.launch { addToCart(productId) }
    }

    fun onRetryCategories() {
        if (_uiState.value.categories is UiState.Error) loadCategories()
    }

    fun onRetrySection(section: HomeSection) {
        if (_uiState.value.sections[section] is UiState.Error) loadSection(section)
    }

    private fun loadCategories() {
        categoriesJob?.cancel()
        _uiState.update { it.copy(categories = UiState.Loading) }
        categoriesJob = viewModelScope.launch {
            val categories = when (val result = getCategories()) {
                is AppResult.Failure -> UiState.Error(result.error)
                is AppResult.Success -> result.data.map { HomeCategoryUi(it.id, it.name, it.imageUrl) }.toUiState()
            }
            _uiState.update { it.copy(categories = categories) }
        }
    }

    private fun loadSection(section: HomeSection) {
        sectionJobs[section]?.cancel()
        _uiState.update { it.copy(sections = it.sections + (section to UiState.Loading)) }
        sectionJobs[section] = viewModelScope.launch {
            val products = when (val result = getProducts(sort = section.sort())) {
                is AppResult.Failure -> UiState.Error(result.error)
                is AppResult.Success -> result.data.items.take(ROW_SIZE).map { it.toCardUi() }.distinctBy { it.id }.toUiState()
            }
            _uiState.update { it.copy(sections = it.sections + (section to products)) }
        }
    }

    private companion object {
        /** A row is a teaser; the full lists live in Categories -> Product list. */
        const val ROW_SIZE = 10
    }
}

private fun HomeSection.sort() = when (this) {
    HomeSection.BestSellers -> ProductSort.BestSelling
    HomeSection.NewArrival -> ProductSort.Newest
    HomeSection.TopRated -> ProductSort.TopRated
}

private fun <T> List<T>.toUiState(): UiState<List<T>> = if (isEmpty()) UiState.Empty else UiState.Success(this)
