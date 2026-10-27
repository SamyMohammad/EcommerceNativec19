package com.example.ecommercenativec19.feature.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.products.domain.GetProductDetailsUseCase
import com.example.ecommercenativec19.feature.products.domain.Product
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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
@HiltViewModel(assistedFactory = ProductDetailsViewModel.Factory::class)
class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted private val productId: String,
    private val getProductDetails: GetProductDetailsUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val addToCart: AddToCartUseCase,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(productId: String): ProductDetailsViewModel
    }

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
        if (_uiState.value.product is UiState.Error) load()
    }

    fun onToggleFavorite() {
        viewModelScope.launch { toggleFavorite(productId) }
    }

    /** Adds the selected quantity on top of whatever the cart already holds for this product. */
    fun onAddToCart() {
        val quantity = _uiState.value.quantity
        // A failed add leaves the cart as it was; no extra error UI in this slice.
        viewModelScope.launch { addToCart(productId, quantity) }
    }

    fun onIncrementQuantity() = _uiState.update { it.copy(quantity = it.quantity + 1) }

    /** The design shows no limits; the quantity just never drops below 1 so the pill cannot show 0 or negatives. */
    fun onDecrementQuantity() = _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(MinQuantity)) }

    private fun load() {
        loadJob?.cancel()
        _uiState.update { it.copy(product = UiState.Loading) }
        loadJob = viewModelScope.launch {
            when (val result = getProductDetails(productId)) {
                is AppResult.Failure -> _uiState.update { it.copy(product = UiState.Error(result.error)) }
                is AppResult.Success -> _uiState.update { it.copy(product = UiState.Success(result.data.toUi())) }
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
