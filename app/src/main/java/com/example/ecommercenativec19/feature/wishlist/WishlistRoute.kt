package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Wishlist entry point. Row tap -> [onOpenProduct]. The row "Add to Cart" buttons do nothing until the cart slice:
 * the prototype's only documented "add to cart" effect is Details -> Cart.
 */
@Composable
fun WishlistRoute(
    onOpenCart: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: WishlistViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Runs on every visit, not just when the ViewModel is created: the tab's ViewModel outlives tab switches.
    LaunchedEffect(viewModel) { viewModel.onEnter() }
    WishlistScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onToggleFavorite = viewModel::onToggleFavorite,
        onProductClick = onOpenProduct,
        onAddToCart = {},
        onOpenCart = onOpenCart,
        onRetry = viewModel::onRetry,
        modifier = modifier,
    )
}
