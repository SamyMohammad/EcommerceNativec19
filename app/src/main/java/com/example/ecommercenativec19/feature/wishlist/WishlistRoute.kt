package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.fake.FakeData
import com.example.ecommercenativec19.fake.LocalFakeStore
import com.example.ecommercenativec19.fake.toCardUi

/**
 * Wishlist entry point. Row tap -> [onOpenProduct]. The row "Add to Cart" button adds one unit to the in-memory cart
 * and stays here; un-hearting a row removes it at once.
 */
@Composable
fun WishlistRoute(
    onOpenCart: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    var query by remember { mutableStateOf("") }
    val favorites = FakeData.products.filter { it.id in store.favoriteIds }.map { it.toCardUi() }
    WishlistScreen(
        state = WishlistUiState(
            query = query,
            items = if (favorites.isEmpty()) LoadState.Empty else LoadState.Content(favorites),
        ),
        onQueryChange = { query = it },
        onToggleFavorite = store::toggleFavorite,
        onProductClick = onOpenProduct,
        onAddToCart = { store.addToCart(it) },
        onOpenCart = onOpenCart,
        onRetry = {},
        modifier = modifier,
    )
}
