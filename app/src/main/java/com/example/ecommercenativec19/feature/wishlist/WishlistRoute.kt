package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Wishlist entry point. The row "Add to Cart" buttons have no behavior in the prototype (the only documented
 * "add to cart" effect is Details -> Cart): wired to a no-op here (missing contract).
 */
@Composable
fun WishlistRoute(onOpenCart: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: WishlistViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    WishlistScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onToggleFavorite = viewModel::onToggleFavorite,
        onAddToCart = {},
        onOpenCart = onOpenCart,
        modifier = modifier,
    )
}
