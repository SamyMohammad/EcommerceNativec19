package com.example.ecommercenativec19.feature.home

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
 * Home entry point. Callback contract: see AppNavDisplay (cart icon, category tile -> Categories, product card -> Details).
 * The search text is UI-only; hearts and the card "+" write to the in-memory store.
 */
@Composable
fun HomeRoute(
    onOpenCart: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenProduct: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    var query by remember { mutableStateOf("") }
    val categories = remember { FakeData.categories.map { HomeCategoryUi(it.id, it.name, it.imageRes) } }
    val sections = remember {
        HomeSection.entries.associateWith { section ->
            LoadState.Content(FakeData.section(section).map { it.toCardUi() })
        }
    }
    HomeScreen(
        state = HomeUiState(
            searchQuery = query,
            categories = LoadState.Content(categories),
            sections = sections,
            favoriteIds = store.favoriteIds.toSet(),
        ),
        onQueryChange = { query = it },
        onFavoriteClick = store::toggleFavorite,
        onAddToCart = { store.addToCart(it) },
        onProductClick = onOpenProduct,
        onOpenCart = onOpenCart,
        onOpenCategories = onOpenCategories,
        onRetryCategories = {},
        onRetrySection = {},
        modifier = modifier,
    )
}
