package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import kotlin.math.roundToInt
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/** The two product card scales in the design. */
enum class ProductCardSize(
    internal val imageHeight: Dp,
    internal val borderWidth: Dp,
    internal val textStart: Dp,
    internal val favorite: FavoriteButtonSize,
    /** Old price start x, measured like [textStart] from the inside of the border. */
    internal val oldPriceStart: Dp,
    /** Rating star start x, measured like [textStart] from the inside of the border (the design pins it there). */
    internal val starStart: Dp,
    /** Regular cards show the price in the 14dp body style, compact ones in the 12dp small style. */
    internal val largePrice: Boolean,
) {
    /** Home: 158 wide, 1dp border, 122dp image, 16dp favorite circle (card height 211, or 235 with old price). */
    Compact(122.dp, 1.dp, 7.dp, FavoriteButtonSize.Small, 78.dp, 93.dp, false),

    /** Product list: 191 wide, 2dp border, 128dp image, 30dp favorite circle (card height 237). */
    Regular(128.dp, 2.dp, 6.dp, FavoriteButtonSize.Regular, 84.dp, 83.dp, true),
}

/** Where the "Review (x.x) *" row goes relative to the price row. Home appliance/New arrival cards put it first. */
enum class ProductCardRatingPosition { AbovePrice, BelowPrice }

/**
 * Vertical product card shared by Home and Product list: image (with the favorite circle in its top-end corner),
 * title, price (+ struck-through old price), rating row and the primary "+" add button at the bottom-end corner.
 *
 * Stateless. Strings are passed already formatted (`"EGP 1,200"`, `"2000 EGP"`), [rating] is just the number
 * (`"4.8"`; shown as "Review (4.8)" + star). [image] draws inside the image area (use `Image(contentScale = Crop)`
 * or [FigmaCroppedImage]). The card has no fixed height: pass `Modifier.height(...)` (211 / 235 / 237 in the
 * design) when rows must align; the add button is pinned to the bottom-end corner.
 */
@Composable
fun ProductCard(
    title: String,
    price: String,
    rating: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onAddClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    size: ProductCardSize = ProductCardSize.Regular,
    oldPrice: String? = null,
    ratingPosition: ProductCardRatingPosition = ProductCardRatingPosition.BelowPrice,
    onClick: (() -> Unit)? = null,
) {
    val tokens = LocalAppTokens.current
    val shape = RoundedCornerShape(tokens.radiusSource02)
    val clickModifier = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .clip(shape)
            .background(tokens.colorSource02)
            .border(size.borderWidth, tokens.colorSource14, shape)
            .then(clickModifier),
    ) {
        Column(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(size.imageHeight)
                    .clip(RoundedCornerShape(topStart = tokens.radiusSource02, topEnd = tokens.radiusSource02)),
            ) {
                image()
                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick,
                    size = size.favorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = FavoriteInset, end = FavoriteInset),
                )
            }
            Column(
                // The design measures text x from inside the border; the border is drawn over the content box here.
                modifier = Modifier.padding(
                    start = size.textStart + size.borderWidth,
                    top = TextTopGap,
                    end = size.textStart + size.borderWidth,
                ),
                verticalArrangement = Arrangement.spacedBy(RowGap),
            ) {
                if ('\n' in title) {
                    LineBoxText(text = title, style = tokens.typographySource07, color = tokens.colorSource12)
                } else {
                    Text(
                        text = title,
                        style = tokens.typographySource07,
                        color = tokens.colorSource12,
                        maxLines = 2,
                        minLines = if (ratingPosition == ProductCardRatingPosition.BelowPrice) 2 else 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (ratingPosition == ProductCardRatingPosition.AbovePrice) RatingRow(rating, size)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = price,
                        style = if (size.largePrice) tokens.typographySource07 else tokens.typographySource11,
                        color = tokens.colorSource12,
                        maxLines = 1,
                        // The old price starts at the design's x, or after a minimum gap when the price is wider.
                        modifier = Modifier
                            .widthIn(min = size.oldPriceStart - size.textStart - OldPriceGap)
                            .textLineBox(if (size.largePrice) tokens.typographySource07 else tokens.typographySource11),
                    )
                    if (oldPrice != null) {
                        Spacer(Modifier.width(OldPriceGap))
                        val strikeColor = tokens.colorSource19
                        Text(
                            text = oldPrice,
                            style = tokens.typographySource13,
                            color = tokens.colorSource19,
                            maxLines = 1,
                            // The design strikes the old price with a separate 1dp line (49dp wide) from the middle of
                            // the 18dp line box downward, which keeps it on whole pixels; a text decoration would
                            // straddle two pixel rows.
                            modifier = Modifier
                                .drawBehind {
                                    val thickness = 1.dp.toPx()
                                    drawRect(
                                        color = strikeColor,
                                        topLeft = Offset(0f, (this.size.height / 2f).roundToInt().toFloat()),
                                        size = Size(this.size.width, thickness),
                                    )
                                }
                                .textLineBox(tokens.typographySource13),
                        )
                    }
                }
                if (ratingPosition == ProductCardRatingPosition.BelowPrice) RatingRow(rating, size)
            }
        }
        Image(
            painter = painterResource(R.drawable.ic_icon_plus_circle_633de255),
            contentDescription = stringResource(R.string.core_add_to_cart),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = AddButtonInset, bottom = AddButtonInset)
                .size(AddButtonSize)
                .clickable(role = Role.Button, onClick = onAddClick),
        )
    }
}

@Composable
private fun RatingRow(rating: String, size: ProductCardSize) {
    val tokens = LocalAppTokens.current
    // The design top-aligns the 15dp star with the 18dp text line and pins it at [ProductCardSize.starStart]
    // (or after a minimum gap when the text is wider).
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = stringResource(R.string.core_review_rating, rating),
            style = tokens.typographySource11,
            color = tokens.colorSource12,
            maxLines = 1,
            modifier = Modifier
                .widthIn(min = (size.starStart - size.textStart - StarGap).coerceAtLeast(0.dp))
                .textLineBox(tokens.typographySource11),
        )
        Spacer(Modifier.width(StarGap))
        Image(
            painter = painterResource(R.drawable.ic_emoji_white_medium_star_8bdf3511),
            contentDescription = null,
            modifier = Modifier.size(StarSize),
        )
    }
}

/** Favorite circle distance from the image's top/end edges. */
private val FavoriteInset = 8.dp

/** Image bottom to title top (also the gap between title / price / rating rows). */
private val TextTopGap = 8.dp
private val RowGap = 8.dp
private val OldPriceGap = 4.dp
private val StarGap = 4.dp
private val StarSize = 15.dp

/** The 30dp "+" button sits 8dp from the card's end and bottom edges. */
private val AddButtonSize = 30.dp
private val AddButtonInset = 8.dp
