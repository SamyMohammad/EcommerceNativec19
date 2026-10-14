package com.example.ecommercenativec19.feature.categories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.fake.FakeData

/** Categories entry point. Callback contract: see AppNavDisplay (cart icon, any sub-category tile -> Product list of that sub-category). */
@Composable
fun CategoriesRoute(
    onOpenCart: () -> Unit,
    onOpenProductList: (subCategoryId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    var selectedId by remember { mutableStateOf(FakeData.categories.first().id) }
    val selected = FakeData.categories.first { it.id == selectedId }
    CategoriesScreen(
        state = CategoriesUiState(
            searchQuery = query,
            categories = LoadState.Content(
                FakeData.categories.map { RailCategory(it.id, it.name, isSelected = it.id == selectedId) },
            ),
            selectedTitle = selected.name,
            subCategories = LoadState.Content(
                FakeData.subCategoriesOf(selectedId).map { SubCategoryItem(it.id, it.name) },
            ),
        ),
        onQueryChange = { query = it },
        onCategorySelected = { selectedId = it },
        onRetry = {},
        onOpenCart = onOpenCart,
        onOpenProductList = onOpenProductList,
        modifier = modifier,
    )
}
