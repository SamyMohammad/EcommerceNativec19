package com.example.ecommercenativec19.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.SearchHeader

/**
 * Stateless Categories screen: fixed logo/search/cart header and bottom bar around the category rail (start) and
 * the selected category's content (banner + sub-category grid, scrolls vertically).
 */
@Composable
fun CategoriesScreen(
    state: CategoriesUiState,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onRetry: () -> Unit,
    onOpenCart: () -> Unit,
    onOpenProductList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        SearchHeader(
            query = state.searchQuery,
            onQueryChange = onQueryChange,
            onCartClick = onOpenCart,
            // The categories frame places the icon at 6.82% (22.99dp) instead of 6.92%.
            iconStart = 22.99.dp,
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = HeaderToRailGap),
        ) {
            CategoriesRail(
                state = state.categories,
                onCategorySelected = onCategorySelected,
                onRetry = onRetry,
                modifier = Modifier.width(RailWidth).fillMaxHeight().overflowBottom(RailUnderBarOverlap),
            )
            CategoriesContent(
                state = state.subCategories,
                title = state.selectedTitle,
                onOpenProductList = onOpenProductList,
                onRetry = onRetry,
                modifier = Modifier.weight(1f).fillMaxHeight().padding(start = RailToContentGap),
            )
        }
    }
}

/** Search field bottom (y 141) to rail / heading top (y 157). */
private val HeaderToRailGap = 16.dp
private val RailWidth = 137.dp

/** Rail right edge (x 153) to content left edge (x 177). */
private val RailToContentGap = 24.dp

/** The design's rail ends at y 881, 5dp below the bottom bar's top edge (y 876): the bar covers its lower end. */
private val RailUnderBarOverlap = 5.dp

/** Measures this element [extra] taller than the space it is given, without taking that space (it overflows). */
private fun Modifier.overflowBottom(extra: Dp): Modifier = layout { measurable, constraints ->
    val extraPx = extra.roundToPx()
    val height = constraints.maxHeight
    val placeable = measurable.measure(constraints.copy(minHeight = height + extraPx, maxHeight = height + extraPx))
    layout(placeable.width, height) { placeable.place(0, 0) }
}
