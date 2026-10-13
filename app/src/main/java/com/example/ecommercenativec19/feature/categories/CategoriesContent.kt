package com.example.ecommercenativec19.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.CoverImage
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.core.ui.CategoryTile
import com.example.ecommercenativec19.core.ui.CategoryTileStyle
import com.example.ecommercenativec19.core.ui.LineBoxText
import com.example.ecommercenativec19.core.ui.textLineBox

/** Right-hand side: selected category title, its promo banner and the three-column sub-category grid. */
@Composable
internal fun CategoriesContent(
    state: LoadState<List<SubCategoryItem>>,
    title: String?,
    onOpenProductList: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusPane(state = state, onRetry = onRetry, modifier = modifier) { subCategories ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(GridColumns),
            contentPadding = PaddingValues(bottom = BottomContentGap),
        ) {
            if (title != null) {
                item(key = "title", span = { GridItemSpan(maxLineSpan) }, contentType = "title") {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.textLineBox(MaterialTheme.typography.labelLarge),
                    )
                }
                item(key = "banner", span = { GridItemSpan(maxLineSpan) }, contentType = "banner") {
                    SelectedCategoryBanner(title, Modifier.padding(top = TitleToBannerGap))
                }
            }
            itemsIndexed(subCategories, key = { _, sub -> sub.id }, contentType = { _, _ -> "tile" }) { index, sub ->
                // The design's tiles start at x 177 / 260 / 344 (gaps of 13 and 14). The grid cells are equal, so each
                // 70dp tile is nudged inside its cell to land on those x positions.
                // The Box hands the tile loose constraints; a grid cell is fixed-width and would stretch the 70dp tile.
                Box(
                    Modifier.padding(
                        start = TileStartInCell[index % GridColumns],
                        top = if (index < GridColumns) BannerToGridGap else GridRowGap,
                    ),
                ) {
                    CategoryTile(
                        label = sub.label,
                        image = { TilePlaceholder() },
                        style = CategoryTileStyle.Square,
                        onClick = onOpenProductList,
                    )
                }
            }
        }
    }
}

/** The API gives sub-categories no image, so every tile shows a neutral block. */
@Composable
private fun TilePlaceholder() {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant))
}

/** Faded photo banner with title and a "Shop Now" pill. The photo is directional artwork, so it stays LTR. */
@Composable
private fun SelectedCategoryBanner(title: String, modifier: Modifier = Modifier) {
    val shape = MaterialTheme.shapes.small
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = BannerMinHeight)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            // The exported photo already carries the design's 80% opacity in its alpha channel (204/255).
            CoverImage(painterResource(R.drawable.img_frame32_617edea3), null, Modifier.matchParentSize())
            // Design-absolute layout inside the 237 x 94 banner: title at y 8, "Shop Now" pill at y 56.
            LineBoxText(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = BannerPadding, top = BannerTop),
            )
            // Visual only: the prototype defines no action for the banner's "Shop Now".
            Box(
                modifier = Modifier
                    .padding(start = BannerPadding, top = ShopNowTop)
                    .size(width = ShopNowWidth, height = ShopNowHeight)
                    .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = stringResource(R.string.categories_shop_now),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = ShopNowTextStart).textLineBox(MaterialTheme.typography.bodySmall),
                )
            }
        }
    }
}

private const val GridColumns = 3
private val TitleToBannerGap = 16.dp
private val BannerToGridGap = 20.dp
private val GridRowGap = 16.dp
private val BottomContentGap = 16.dp
private val BannerMinHeight = 94.dp
private val BannerPadding = 16.dp
private val BannerTop = 8.dp
private val ShopNowTop = 56.dp

/** The design left-aligns the pill's label 14dp from the pill's start edge. */
private val ShopNowTextStart = 14.dp
private val ShopNowWidth = 97.dp
private val ShopNowHeight = 30.dp

/** Cells are 79dp wide (237 / 3), tiles 70dp: start offsets that put the tiles at x 0 / 83 / 167 of the 237dp content. */
private val TileStartInCell = listOf(0.dp, 4.dp, 9.dp)
