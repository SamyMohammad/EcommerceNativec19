package com.example.ecommercenativec19.feature.productdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

/**
 * Product details entry point. [productId] comes from the nav route and is handed to the ViewModel through a
 * simple factory (keyed by id so each product gets its own state). Back -> [onBack]; cart icon -> [onOpenCart];
 * "Add to cart" -> [onAddToCart] (no other effect is defined by the prototype).
 */
@Composable
fun ProductDetailsRoute(
    productId: String,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProductDetailsViewModel = viewModel(
        key = productId,
        factory = viewModelFactory { initializer { ProductDetailsViewModel(productId) } },
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProductDetailsScreen(
        state = state,
        onBack = onBack,
        onCartClick = onOpenCart,
        onAddToCart = onAddToCart,
        onFavoriteClick = viewModel::onToggleFavorite,
        onSizeSelected = viewModel::onSizeSelected,
        onColorSelected = viewModel::onColorSelected,
        onIncrementQuantity = viewModel::onIncrementQuantity,
        onDecrementQuantity = viewModel::onDecrementQuantity,
        onImagePageChanged = viewModel::onImagePageChanged,
        modifier = modifier,
    )
}
