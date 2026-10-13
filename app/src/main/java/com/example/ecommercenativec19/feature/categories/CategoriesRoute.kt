package com.example.ecommercenativec19.feature.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommercenativec19.core.ui.AppTab
import org.koin.androidx.compose.koinViewModel

/** Categories entry point. Callback contract: see AppNavHost (tabs, cart icon, any sub-category tile -> Product list). */
@Composable
fun CategoriesRoute(
    onTabSelected: (AppTab) -> Unit,
    onOpenCart: () -> Unit,
    onOpenProductList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CategoriesViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoriesScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        onCategorySelected = viewModel::onCategorySelected,
        onRetry = viewModel::onRetry,
        onTabSelected = onTabSelected,
        onOpenCart = onOpenCart,
        onOpenProductList = onOpenProductList,
        modifier = modifier,
    )
}
