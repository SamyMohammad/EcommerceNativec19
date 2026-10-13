package com.example.ecommercenativec19.feature.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.domain.GetProductsUseCase
import com.example.ecommercenativec19.feature.products.ui.toCardUi
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Products of one sub-category, loaded page by page. The search text is UI-only (no search endpoint is used). Hearts
 * mirror the shared wishlist cache; a failed toggle is rolled back there, so the heart returns to its previous state.
 */
class ProductListViewModel(
    private val subCategoryId: String,
    private val getProducts: GetProductsUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    /** Not UI state: the screen only needs to know whether a footer is showing, not which page is next. */
    private var nextPage: Int? = null
    private var firstPageJob: Job? = null
    private var loadMoreJob: Job? = null

    init {
        viewModelScope.launch {
            observeFavoriteIds().collect { ids -> _uiState.update { it.copy(favoriteIds = ids) } }
        }
        loadFirstPage()
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onToggleFavorite(productId: String) {
        viewModelScope.launch { toggleFavorite(productId) }
    }

    /** Retries a failed first page. A failed "load more" is retried through [onLoadMore]. */
    fun onRetry() {
        if (_uiState.value.products is LoadState.Error) loadFirstPage()
    }

    /** Called when the list scrolls near its end. Ignored while a page is loading or when there is no next page. */
    fun onLoadMore() {
        val page = nextPage ?: return
        val state = _uiState.value
        if (state.products !is LoadState.Content || state.loadMore == LoadMoreState.Loading) return
        // Set before launching so a second scroll event cannot start a duplicate request.
        _uiState.update { it.copy(loadMore = LoadMoreState.Loading) }
        loadMoreJob = viewModelScope.launch {
            when (val result = getProducts(subCategoryId = subCategoryId, page = page)) {
                is AppResult.Failure -> _uiState.update { it.copy(loadMore = LoadMoreState.Error) }
                is AppResult.Success -> {
                    nextPage = result.data.nextPage
                    _uiState.update { current ->
                        val loaded = (current.products as? LoadState.Content)?.data.orEmpty()
                        current.copy(
                            // Items can shift between requests; a duplicate id would crash the lazy grid's keys.
                            products = LoadState.Content((loaded + result.data.items.map { it.toCardUi() }).distinctBy { it.id }),
                            loadMore = LoadMoreState.Idle,
                        )
                    }
                }
            }
        }
    }

    private fun loadFirstPage() {
        firstPageJob?.cancel()
        loadMoreJob?.cancel()
        nextPage = null
        _uiState.update { it.copy(products = LoadState.Loading, loadMore = LoadMoreState.Idle) }
        firstPageJob = viewModelScope.launch {
            when (val result = getProducts(subCategoryId = subCategoryId)) {
                is AppResult.Failure -> _uiState.update { it.copy(products = LoadState.Error(result.error)) }
                is AppResult.Success -> {
                    nextPage = result.data.nextPage
                    val cards = result.data.items.map { it.toCardUi() }.distinctBy { it.id }
                    _uiState.update {
                        it.copy(products = if (cards.isEmpty()) LoadState.Empty else LoadState.Content(cards))
                    }
                }
            }
        }
    }
}
