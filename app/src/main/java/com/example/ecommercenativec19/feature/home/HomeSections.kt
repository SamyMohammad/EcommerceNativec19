package com.example.ecommercenativec19.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.CategoryTile
import com.example.ecommercenativec19.core.ui.CategoryTileStyle
import com.example.ecommercenativec19.core.ui.ProductCard
import com.example.ecommercenativec19.core.ui.ProductCardRatingPosition
import com.example.ecommercenativec19.core.ui.ProductImage
import com.example.ecommercenativec19.core.ui.formatOldPrice
import com.example.ecommercenativec19.core.ui.formatPrice
import com.example.ecommercenativec19.core.ui.formatRating
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi
import com.example.ecommercenativec19.core.ui.ProductCardSize
import com.example.ecommercenativec19.core.ui.textLineBox

/** Section title (Medium 18) with an optional end-aligned small link-style label ("view all"). */
@Composable
internal fun HomeSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailing: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = SectionGap, bottom = TitleToContentGap),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
        )
        if (trailing != null) {
            // The prototype defines no destination for "view all", so it is plain text, not a button.
            Text(
                text = trailing,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.textLineBox(MaterialTheme.typography.bodySmall),
            )
        }
    }
}

/**
 * Two-row category grid that scrolls horizontally (the design shows the last column cut by the screen edge).
 * Tiles fill the rows column by column: (1st, 2nd), (3rd, 4th), ... Every tile opens the Categories tab.
 */
@Composable
internal fun HomeCategoriesGrid(
    categories: List<HomeCategoryUi>,
    onOpenCategories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = remember(categories) { categories.chunked(2) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = AppDimens.ScreenHorizontal),
        horizontalArrangement = Arrangement.spacedBy(GridGap),
    ) {
        columns.forEach { column ->
            Column(verticalArrangement = Arrangement.spacedBy(GridGap)) {
                column.forEachIndexed { index, category ->
                    CategoryTile(
                        label = category.name,
                        image = { ProductImage(url = category.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize()) },
                        // Names take two lines in the design; keep both rows aligned across columns.
                        modifier = if (index == 0) Modifier.heightIn(min = TopTileMinHeight) else Modifier,
                        style = CategoryTileStyle.Circle,
                        onClick = onOpenCategories,
                    )
                }
            }
        }
    }
}

/** Horizontally scrolling row of Home product cards (158dp wide, 16dp apart, cut at the screen edge). */
@Composable
internal fun HomeProductRow(
    products: List<ProductCardUi>,
    favoriteIds: Set<String>,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = AppDimens.ScreenHorizontal),
        horizontalArrangement = Arrangement.spacedBy(CardGap),
    ) {
        items(products, key = { it.id }, contentType = { "product" }) { product ->
            ProductCard(
                title = product.title,
                price = formatPrice(product.price),
                oldPrice = product.oldPrice?.let { formatOldPrice(it) },
                rating = formatRating(product.rating),
                isFavorite = product.id in favoriteIds,
                onFavoriteClick = { onFavoriteClick(product.id) },
                // Prototype: the "+" add button has no documented behavior (no cart state contract).
                onAddClick = {},
                onClick = { onProductClick(product.id) },
                image = { ProductImage(url = product.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize()) },
                // Any API product may be discounted, so every card gets the taller (old price) design height.
                modifier = Modifier.width(CardWidth).heightIn(min = HomeCardHeight),
                size = ProductCardSize.Compact,
                ratingPosition = ProductCardRatingPosition.BelowPrice,
            )
        }
    }
}

private val GridGap = 16.dp
private val CardGap = 16.dp
private val CardWidth = 158.dp

/** 100dp circle + 8dp gap + two label lines (about 19dp each at the 14sp body style). */
private val TopTileMinHeight = 146.dp

/** Title to content gap (title bottom y 242 -> content y 258). */
private val TitleToContentGap = 16.dp
