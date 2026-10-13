package com.example.ecommercenativec19.feature.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommercenativec19.core.ui.AppTab

/** Categories entry point. Callback contract: see AppNavHost (tabs, cart icon, "Footwear" tile -> Product list). */
@Composable
fun CategoriesRoute(
    onTabSelected: (AppTab) -> Unit,
    onOpenCart: () -> Unit,
    onOpenProductList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: CategoriesViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CategoriesScreen(
        state = state,
        onQueryChange = viewModel::onSearchQueryChange,
        onTabSelected = onTabSelected,
        onOpenCart = onOpenCart,
        onOpenProductList = onOpenProductList,
        modifier = modifier,
    )
}
