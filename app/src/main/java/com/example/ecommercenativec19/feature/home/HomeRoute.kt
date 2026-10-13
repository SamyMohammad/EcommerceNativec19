package com.example.ecommercenativec19.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommercenativec19.core.ui.AppTab

/** Home entry point. Callback contract: see AppNavHost (tabs, cart icon, "men's fashion" circle -> Categories). */
@Composable
fun HomeRoute(
    onTabSelected: (AppTab) -> Unit,
    onOpenCart: () -> Unit,
    onOpenCategories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        onFavoriteClick = viewModel::onFavoriteToggle,
        onTabSelected = onTabSelected,
        onOpenCart = onOpenCart,
        onOpenCategories = onOpenCategories,
        modifier = modifier,
    )
}
