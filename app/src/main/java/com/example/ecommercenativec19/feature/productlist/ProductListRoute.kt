package com.example.ecommercenativec19.feature.productlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Product list entry point for one sub-category. Card click -> [onOpenProduct] with the opaque product id. The card
 * "+" button has no documented behavior in the prototype, so it does nothing yet.
 */
@Composable
fun ProductListRoute(
    subCategoryId: String,
    onOpenCart: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProductListViewModel = koinViewModel(key = subCategoryId) { parametersOf(subCategoryId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProductListScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onCartClick = onOpenCart,
        onProductClick = onOpenProduct,
        onFavoriteClick = viewModel::onToggleFavorite,
        onAddClick = { /* No behavior defined by the prototype. */ },
        onRetry = viewModel::onRetry,
        onLoadMore = viewModel::onLoadMore,
        modifier = modifier,
    )
}
