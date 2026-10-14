package com.example.ecommercenativec19.feature.productlist

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
 * Product list entry point for one sub-category. Card click -> [onOpenProduct]. The card "+" button adds one unit to
 * the in-memory cart and stays on the list. There is a single page, so "load more" and retry do nothing.
 */
@Composable
fun ProductListRoute(
    subCategoryId: String,
    onOpenCart: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    var query by remember { mutableStateOf("") }
    val products = remember(subCategoryId) { FakeData.productsIn(subCategoryId).map { it.toCardUi() } }
    ProductListScreen(
        state = ProductListUiState(
            query = query,
            products = LoadState.Content(products),
            favoriteIds = store.favoriteIds.toSet(),
        ),
        onQueryChange = { query = it },
        onCartClick = onOpenCart,
        onProductClick = onOpenProduct,
        onFavoriteClick = store::toggleFavorite,
        onAddClick = { store.addToCart(it) },
        onRetry = {},
        onLoadMore = {},
        modifier = modifier,
    )
}
