package com.example.ecommercenativec19.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Home entry point. Callback contract: see AppNavDisplay (cart icon, category tile -> Categories, product card -> Details). */
@Composable
fun HomeRoute(
    onOpenCart: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        onFavoriteClick = viewModel::onFavoriteToggle,
        onAddToCart = viewModel::onAddToCart,
        onProductClick = onOpenProduct,
        onOpenCart = onOpenCart,
        onOpenCategories = onOpenCategories,
        onRetryCategories = viewModel::onRetryCategories,
        onRetrySection = viewModel::onRetrySection,
        modifier = modifier,
    )
}
