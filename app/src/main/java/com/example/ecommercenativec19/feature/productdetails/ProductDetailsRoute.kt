package com.example.ecommercenativec19.feature.productdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Product details entry point. [productId] comes from the nav route and is handed to the ViewModel through
 * Koin parameters (keyed by id so each product gets its own state). Back -> [onBack]; cart icon -> [onOpenCart];
 * "Add to cart" adds the selected quantity, then calls [onAddToCart] (the prototype's
 * transition to Cart; the add itself keeps running in the repository after the screen is left).
 */
@Composable
fun ProductDetailsRoute(
    productId: String,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProductDetailsViewModel = koinViewModel(key = productId) { parametersOf(productId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProductDetailsScreen(
        state = state,
        onBack = onBack,
        onCartClick = onOpenCart,
        onAddToCart = {
            viewModel.onAddToCart()
            onAddToCart()
        },
        onFavoriteClick = viewModel::onToggleFavorite,
        onIncrementQuantity = viewModel::onIncrementQuantity,
        onDecrementQuantity = viewModel::onDecrementQuantity,
        onRetry = viewModel::onRetry,
        modifier = modifier,
    )
}
