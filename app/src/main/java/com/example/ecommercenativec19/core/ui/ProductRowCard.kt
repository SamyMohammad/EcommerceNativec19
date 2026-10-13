package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Horizontal 398 x 113 product row shared by Cart and Wishlist: 120dp image on the start side, then title (Medium 18),
 * a color dot with variant text ("Orange" | "Size: 40", "Black color"), and the price (+ struck-through old price).
 *
 * Two corner slots keep the screens' differences out of the component:
 * - [topEnd]: delete icon (Cart) or [FavoriteButton] (Wishlist), 8dp from the top and the end.
 * - [bottomEnd]: [QuantitySelector] (Cart) or "Add to Cart" [AppButton] (Wishlist), 8dp from bottom and end.
 * (The Wishlist heart and button sit 16dp from the end in the design: add `Modifier.padding(end = 8.dp)` there.)
 *
 * Offsets are measured from the outer edge of the 113dp row (the design's absolute children sit inside a 1dp border,
 * so a design `top-[46px]` is 47 here). Cart and Wishlist place the text lines 1-2dp differently, so [titleTop],
 * [variantTop] and [priceTop] (top of each 18dp line box) are parameters; the defaults are the Wishlist values.
 * Text lines use [textLineBox] so each is exactly the design's 18dp tall.
 *
 * Stateless; text arrives formatted. [colorDot] is a theme color (see `ProductVariantDot.tokenColor`).
 */
@Composable
fun ProductRowCard(
    title: String,
    price: String,
    image: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    oldPrice: String? = null,
    colorDot: Color? = null,
    variantColor: Color? = null,
    titleTop: Dp = 14.dp,
    variantTop: Dp = 46.dp,
    priceTop: Dp = 79.dp,
    variantLabel: String? = null,
    variantDetail: String? = null,
    topEnd: @Composable BoxScope.() -> Unit = {},
    bottomEnd: @Composable BoxScope.() -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    val shape = MaterialTheme.shapes.medium
    val variantTextColor = variantColor ?: MaterialTheme.colorScheme.onSurfaceVariant
    val clickModifier = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(RowHeight)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape)
            .then(clickModifier),
    ) {
        Row(Modifier.fillMaxHeight()) {
            Box(
                Modifier
                    .width(ImageWidth)
                    .fillMaxHeight()
                    .clip(shape)
                    .border(1.dp, MaterialTheme.colorScheme.outline, shape),
                content = image,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = TextStart, top = titleTop),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium).padding(end = TitleEndReserve),
                )
                Spacer(Modifier.height(variantTop - titleTop - LineHeight))
                if (colorDot != null || variantLabel != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (colorDot != null) {
                            Box(Modifier.size(DotSize).background(colorDot, CircleShape))
                            Spacer(Modifier.width(DotGap))
                        }
                        if (variantLabel != null) {
                            Text(
                                text = variantLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                color = variantTextColor,
                                maxLines = 1,
                                modifier = Modifier.textLineBox(MaterialTheme.typography.bodyMedium),
                            )
                        }
                        if (variantDetail != null) {
                            Spacer(Modifier.width(DividerGap))
                            Box(
                                Modifier
                                    .size(width = DividerWidth, height = DividerHeight)
                                    .background(MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large),
                            )
                            Spacer(Modifier.width(DividerGap))
                            Text(
                                text = variantDetail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = variantTextColor,
                                maxLines = 1,
                                modifier = Modifier.textLineBox(MaterialTheme.typography.bodyMedium),
                            )
                        }
                    }
                }
                if (colorDot == null && variantLabel == null) Spacer(Modifier.height(LineHeight))
                Spacer(Modifier.height(priceTop - variantTop - LineHeight))
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
                    )
                    if (oldPrice != null) {
                        Spacer(Modifier.width(OldPriceGap))
                        Text(
                            text = oldPrice,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough,
                            maxLines = 1,
                            // The design sets the 11sp old price on the 18sp price's baseline: 2dp lower.
                            modifier = Modifier.offset(y = OldPriceDrop).textLineBox(MaterialTheme.typography.labelSmall),
                        )
                    }
                }
            }
        }
        Box(Modifier.align(Alignment.TopEnd).padding(top = CornerInset, end = CornerInset)) { topEnd() }
        Box(Modifier.align(Alignment.BottomEnd).padding(bottom = CornerInset, end = CornerInset)) { bottomEnd() }
    }
}

private val RowHeight = 113.dp
private val ImageWidth = 120.dp
private val TextStart = 8.dp

/** The design's text line box (lineHeight 18) used to turn absolute tops into gaps. */
private val LineHeight = 18.dp
private val TitleEndReserve = 40.dp
private val DotSize = 15.dp
private val DotGap = 8.dp
private val DividerWidth = 2.dp
private val DividerHeight = 15.dp
private val DividerGap = 4.dp
private val OldPriceGap = 8.dp
private val OldPriceDrop = 2.dp
private val CornerInset = 8.dp
