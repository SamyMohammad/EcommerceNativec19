package com.example.ecommercenativec19.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/** Home entry point. Callback contract: see AppNavDisplay (cart icon, category tile -> Categories, product card -> Details). */
@Composable
fun HomeRoute(
    onOpenCart: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        onFavoriteClick = viewModel::onFavoriteToggle,
        onProductClick = onOpenProduct,
        onOpenCart = onOpenCart,
        onOpenCategories = onOpenCategories,
        onRetryCategories = viewModel::onRetryCategories,
        onRetrySection = viewModel::onRetrySection,
        modifier = modifier,
    )
}
