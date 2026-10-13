package com.example.ecommercenativec19.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.CategoryTile
import com.example.ecommercenativec19.core.ui.CategoryTileStyle
import com.example.ecommercenativec19.core.ui.FigmaCropImage
import com.example.ecommercenativec19.core.ui.FigmaImageCrop
import com.example.ecommercenativec19.core.ui.ProductCard
import com.example.ecommercenativec19.core.ui.ProductCardRatingPosition
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
 * Two-row category grid that scrolls horizontally (the design shows the fourth column cut by the screen edge).
 * [categories] is column-major (pairs of top/bottom tiles).
 */
@Composable
internal fun HomeCategoriesGrid(
    categories: List<HomeCategory>,
    onOpenCategories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = AppDimens.ScreenHorizontal),
    ) {
        categories.chunked(2).forEachIndexed { columnIndex, column ->
            if (columnIndex > 0) Spacer(Modifier.width(column.first().columnGap))
            Column(verticalArrangement = Arrangement.spacedBy(GridGap)) {
                column.forEachIndexed { index, category ->
                    CategoryTile(
                        label = stringResource(category.label),
                        image = {
                            // A custom photo width stretches the (square) photo like the design does.
                            FigmaCropImage(
                                category.image,
                                crop = if (category.imageWidth != null) FigmaImageCrop() else null,
                                modifier = Modifier.fillMaxSize(),
                            )
                        },
                        // Top-row labels take two lines in the design; keep both rows aligned across columns.
                        modifier = if (index == 0) Modifier.heightIn(min = TopTileMinHeight) else Modifier,
                        style = CategoryTileStyle.Circle,
                        onClick = if (category.linksToCategories) onOpenCategories else null,
                        imageWidth = category.imageWidth ?: CategoryTileStyle.Circle.imageSize,
                        imageOffsetX = category.imageOffsetX,
                        labelOffsetX = category.labelOffsetX,
                    )
                }
            }
        }
    }
}

/** Horizontally scrolling row of Home product cards (158dp wide, 16dp apart, cut at the screen edge). */
@Composable
internal fun HomeProductRow(
    products: List<HomeProduct>,
    favoriteIds: Set<String>,
    onFavoriteClick: (String) -> Unit,
    ratingPosition: ProductCardRatingPosition,
    minCardHeight: Dp,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = AppDimens.ScreenHorizontal),
        horizontalArrangement = Arrangement.spacedBy(CardGap),
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(
                title = stringResource(product.title),
                price = stringResource(R.string.home_price_egp, product.price),
                oldPrice = product.oldPrice?.let { stringResource(R.string.home_old_price_egp, it) },
                rating = product.rating,
                isFavorite = product.id in favoriteIds,
                onFavoriteClick = { onFavoriteClick(product.id) },
                // Prototype: the "+" add button has no documented behavior (no cart state contract).
                onAddClick = {},
                image = { FigmaCropImage(product.image, product.imageCrop, Modifier.fillMaxSize()) },
                modifier = Modifier.width(CardWidth).heightIn(min = minCardHeight),
                size = ProductCardSize.Compact,
                ratingPosition = ratingPosition,
            )
        }
    }
}

private val GridGap = 16.dp
private val CardGap = 16.dp
private val CardWidth = 158.dp

/** 100dp circle + 8dp gap + two 18dp label lines. */
private val TopTileMinHeight = 144.dp

/** Title to content gap (title bottom y 242 -> content y 258). */
private val TitleToContentGap = 16.dp
