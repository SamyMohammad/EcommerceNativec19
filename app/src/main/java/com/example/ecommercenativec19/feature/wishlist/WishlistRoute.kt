package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Wishlist entry point. Row tap -> [onOpenProduct]. The row "Add to Cart" button adds one unit to the cart and stays here.
 */
@Composable
fun WishlistRoute(
    onOpenCart: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: WishlistViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Runs on every visit, not just when the ViewModel is created: the tab's ViewModel outlives tab switches.
    LaunchedEffect(viewModel) { viewModel.onEnter() }
    WishlistScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onToggleFavorite = viewModel::onToggleFavorite,
        onProductClick = onOpenProduct,
        onAddToCart = viewModel::onAddToCart,
        onOpenCart = onOpenCart,
        onRetry = viewModel::onRetry,
        modifier = modifier,
    )
}
