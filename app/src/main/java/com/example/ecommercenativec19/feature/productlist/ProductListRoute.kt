package com.example.ecommercenativec19.feature.productlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommercenativec19.core.ui.AppTab

/**
 * Product list entry point. Card click -> [onOpenProduct] with the opaque product id. The card "+" button has no
 * documented behavior in the prototype, so it does nothing yet.
 */
@Composable
fun ProductListRoute(
    onTabSelected: (AppTab) -> Unit,
    onOpenCart: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ProductListViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProductListScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onTabSelected = onTabSelected,
        onCartClick = onOpenCart,
        onProductClick = onOpenProduct,
        onFavoriteClick = viewModel::onToggleFavorite,
        onAddClick = { /* No behavior defined by the prototype. */ },
        modifier = modifier,
    )
}
