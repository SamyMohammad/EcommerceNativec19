package com.example.ecommercenativec19.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ecommercenativec19.core.ui.CategoryTile
import com.example.ecommercenativec19.core.ui.CategoryTileStyle
import com.example.ecommercenativec19.core.ui.LineBoxText
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/** Right-hand side: selected category title, its promo banner and the three-column sub-category grid. */
@Composable
internal fun CategoriesContent(
    status: LoadStatus,
    title: String?,
    subCategories: List<SubCategoryItem>,
    onOpenProductList: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalAppTokens.current
    StatusPane(status = status, onRetry = onRetry, modifier = modifier) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            if (title != null) {
                Text(
                    text = title,
                    style = tokens.typographySource15,
                    color = tokens.colorSource12,
                    modifier = Modifier.textLineBox(tokens.typographySource15),
                )
                SelectedCategoryBanner(title, Modifier.padding(top = TitleToBannerGap))
            }
            Column(
                modifier = Modifier.padding(top = BannerToGridGap, bottom = BottomContentGap),
                verticalArrangement = Arrangement.spacedBy(GridRowGap),
            ) {
                subCategories.chunked(GridColumns).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        row.forEachIndexed { index, sub ->
                            // The design's tiles start at x 177 / 260 / 344: gaps of 13 and 14 (SpaceBetween would put the
                            // middle tile on the half pixel 260.5).
                            if (index > 0) Spacer(Modifier.width(if (index == 1) FirstTileGap else SecondTileGap))
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
    }
}

/** The API gives sub-categories no image, so every tile shows a neutral block. */
@Composable
private fun TilePlaceholder() {
    Box(Modifier.fillMaxSize().background(LocalAppTokens.current.colorSource22))
}

/** Faded photo banner with title and a "Shop Now" pill. The photo is directional artwork, so it stays LTR. */
@Composable
private fun SelectedCategoryBanner(title: String, modifier: Modifier = Modifier) {
    val tokens = LocalAppTokens.current
    val shape = RoundedCornerShape(tokens.radiusSource05)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = BannerMinHeight)
                .clip(shape)
                .background(tokens.colorSource23),
        ) {
            // The exported photo already carries the design's 80% opacity in its alpha channel (204/255).
            CoverImage(painterResource(R.drawable.img_frame32_617edea3), null, Modifier.matchParentSize())
            // Design-absolute layout inside the 237 x 94 banner: title at y 8, "Shop Now" pill at y 56.
            LineBoxText(
                text = title,
                style = tokens.typographySource16,
                color = tokens.colorSource12,
                modifier = Modifier.padding(start = BannerPadding, top = BannerTop),
            )
            // Visual only: the prototype defines no action for the banner's "Shop Now".
            Box(
                modifier = Modifier
                    .padding(start = BannerPadding, top = ShopNowTop)
                    .size(width = ShopNowWidth, height = ShopNowHeight)
                    .background(tokens.colorSource00, RoundedCornerShape(tokens.radiusSource05)),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = stringResource(R.string.categories_shop_now),
                    style = tokens.typographySource11,
                    color = tokens.colorSource02,
                    modifier = Modifier.padding(start = ShopNowTextStart).textLineBox(tokens.typographySource11),
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

private val FirstTileGap = 13.dp
private val SecondTileGap = 14.dp
