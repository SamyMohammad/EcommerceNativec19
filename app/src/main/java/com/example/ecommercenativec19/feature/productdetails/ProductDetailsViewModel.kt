package com.example.ecommercenativec19.feature.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.domain.GetProductDetailsUseCase
import com.example.ecommercenativec19.feature.products.domain.Product
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Loads one product by id. Quantity is UI-only state; the heart mirrors the shared wishlist cache, and a failed toggle
 * is rolled back there.
 */
class ProductDetailsViewModel(
    private val productId: String,
    private val getProductDetails: GetProductDetailsUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            observeFavoriteIds().collect { ids -> _uiState.update { it.copy(isFavorite = productId in ids) } }
        }
        load()
    }

    fun onRetry() {
        if (_uiState.value.product is LoadState.Error) load()
    }

    fun onToggleFavorite() {
        viewModelScope.launch { toggleFavorite(productId) }
    }

    fun onIncrementQuantity() = _uiState.update { it.copy(quantity = it.quantity + 1) }

    /** The design shows no limits; the quantity just never drops below 1 so the pill cannot show 0 or negatives. */
    fun onDecrementQuantity() = _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(MinQuantity)) }

    private fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(product = LoadState.Loading) }
        loadJob = viewModelScope.launch {
            when (val result = getProductDetails(productId)) {
                is AppResult.Failure -> _uiState.update { it.copy(product = LoadState.Error(result.error)) }
                is AppResult.Success -> _uiState.update { it.copy(product = LoadState.Content(result.data.toUi())) }
            }
        }
    }

    private companion object {
        const val MinQuantity = 1
    }
}

private fun Product.toUi() = ProductDetailsUi(
    id = id,
    title = title,
    unitPrice = currentPrice,
    soldCount = soldCount,
    rating = rating,
    ratingCount = ratingCount,
    description = description,
    images = images,
)
