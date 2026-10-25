package com.example.ecommercenativec19.feature.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** Categories entry point. Callback contract: see AppNavDisplay (cart icon, any sub-category tile -> Product list of that sub-category). */
@Composable
fun CategoriesRoute(
    onOpenCart: () -> Unit,
    onOpenProductList: (subCategoryId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CategoriesViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoriesScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        onCategorySelected = viewModel::onCategorySelected,
        onRetry = viewModel::onRetry,
        onOpenCart = onOpenCart,
        onOpenProductList = onOpenProductList,
        modifier = modifier,
    )
}
