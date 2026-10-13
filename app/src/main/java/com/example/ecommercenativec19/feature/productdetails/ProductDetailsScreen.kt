package com.example.ecommercenativec19.feature.productdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.QuantitySelector
import com.example.ecommercenativec19.core.ui.ScreenTitleBar
import com.example.ecommercenativec19.core.ui.StatusPane
import com.example.ecommercenativec19.core.ui.centeredBorder
import com.example.ecommercenativec19.core.ui.formatAmount
import com.example.ecommercenativec19.core.ui.formatPrice
import com.example.ecommercenativec19.core.ui.formatRating
import com.example.ecommercenativec19.core.ui.textLineBox

/**
 * Stateless Product details: title bar, then (once loaded) scrollable content (carousel, title/price, sold + rating +
 * quantity, description) and a pinned footer with the total price and "Add to cart". The API has no size or color
 * variants, so there are no option pickers.
 */
@Composable
fun ProductDetailsScreen(
    state: ProductDetailsUiState,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    onAddToCart: () -> Unit,
    onFavoriteClick: () -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        ScreenTitleBar(
            title = stringResource(R.string.details_title),
            onBackClick = onBack,
            // The design shows a search icon, but the prototype defines no behavior for it.
            onSearchClick = {},
            onCartClick = onCartClick,
        )
        StatusPane(
            state = state.product,
            onRetry = onRetry,
            // A single product by id is never "empty": it loads or fails.
            emptyText = stringResource(R.string.error_unknown),
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) { product ->
            Column(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        // The carousel starts at y 90, 2dp inside the 48dp title bar band that ends at 92.
                        .offset(y = -CarouselOverlap)
                        .verticalScroll(rememberScrollState()),
                ) {
                    ProductDetailsImageCarousel(
                        images = product.images,
                        isFavorite = state.isFavorite,
                        onFavoriteClick = onFavoriteClick,
                        modifier = Modifier.padding(horizontal = AppDimens.ScreenHorizontal),
                    )
                    Spacer(Modifier.size(SectionGap + CarouselToTitleExtra))
                    ProductDetailsContent(
                        product = product,
                        quantity = state.quantity,
                        onIncrementQuantity = onIncrementQuantity,
                        onDecrementQuantity = onDecrementQuantity,
                    )
                    Spacer(Modifier.size(SectionGap))
                }
                ProductDetailsFooter(totalPrice = state.totalPrice, onAddToCart = onAddToCart)
            }
        }
    }
}

@Composable
private fun ProductDetailsContent(
    product: ProductDetailsUi,
    quantity: Int,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimens.ScreenHorizontal),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f).textLineBox(MaterialTheme.typography.titleMedium),
            )
            Spacer(Modifier.width(SectionGap))
            Text(
                text = formatPrice(product.unitPrice),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
            )
        }
        Spacer(Modifier.size(SectionGap))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimens.ScreenHorizontal),
            // The design top-aligns the sold pill (34dp), the rating and the quantity pill (42dp) at y 448.
            verticalAlignment = Alignment.Top,
        ) {
            val pillShape = MaterialTheme.shapes.large
            Box(
                modifier = Modifier
                    .heightIn(min = SoldPillHeight)
                    // The sold pill's stroke is centered on its edge in the design (half a dp outside the box).
                    .centeredBorder(1.dp, MaterialTheme.colorScheme.outline, pillShape)
                    .padding(horizontal = SoldPillHorizontalPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.details_sold, formatAmount(product.soldCount)),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.textLineBox(MaterialTheme.typography.labelLarge),
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(SoldPillHeight)
                    .padding(start = SoldToRatingGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_emoji_white_medium_star_8bdf3511),
                    contentDescription = null,
                    modifier = Modifier.size(StarSize),
                )
                Spacer(Modifier.width(StarGap))
                Text(
                    text = stringResource(R.string.details_rating, formatRating(product.rating), formatAmount(product.ratingCount)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.textLineBox(MaterialTheme.typography.bodyMedium),
                )
            }
            QuantitySelector(
                quantity = quantity,
                onIncrement = onIncrementQuantity,
                onDecrement = onDecrementQuantity,
            )
        }
        Spacer(Modifier.size(HeadingGap))
        SectionHeading(stringResource(R.string.details_description_heading))
        Spacer(Modifier.size(HeadingGap))
        Text(
            text = buildAnnotatedString {
                append(product.description)
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) { append(stringResource(R.string.details_read_more)) }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            // The multi-line text keeps the font's natural first-line ascent (a line box of 18dp cannot be forced on
            // it), so it is drawn 1dp up to land on the design's y.
            modifier = Modifier
                .offset(y = -DescriptionRaise)
                .padding(start = AppDimens.ScreenHorizontal, end = DescriptionEndPadding),
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium).padding(horizontal = AppDimens.ScreenHorizontal),
    )
}

@Composable
private fun ProductDetailsFooter(totalPrice: Int, onAddToCart: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = SectionGap, bottom = FooterBottomMargin),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(TotalLabelGap)) {
            Text(
                text = stringResource(R.string.details_total_price),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
            )
            Text(
                text = formatPrice(totalPrice),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
            )
        }
        AppButton(
            text = stringResource(R.string.core_add_to_cart),
            onClick = onAddToCart,
            leadingIcon = painterResource(R.drawable.ic_cart21_84081403),
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(start = SectionGap)
                .widthIn(max = AddToCartMaxWidth),
        )
    }
}

/** Vertical rhythm between sections in the design (e.g. 482 -> 498, 578 -> 594). */
private val SectionGap = 16.dp
private val HeadingGap = 8.dp

/** Carousel bottom (y 390) to title top (y 414) is 24; [SectionGap] already covers 16. */
private val CarouselToTitleExtra = 8.dp

/** "Total price" top (y 780) to the price top (y 810), minus the 18dp line. */
private val TotalLabelGap = 12.dp

/** Add to cart bottom (y 828) to the frame bottom (932). */
private val FooterBottomMargin = 104.dp

/** The design's description box is 381dp wide (x 16..397), which decides the line breaks. */
private val DescriptionEndPadding = 33.dp
private val DescriptionRaise = 1.dp

/** Carousel top (y 90) is above the title bar band's bottom (y 92). */
private val CarouselOverlap = 2.dp
private val SoldPillHeight = 34.dp
private val SoldPillHorizontalPadding = 16.dp
private val SoldToRatingGap = 16.dp
private val StarSize = 15.dp
private val StarGap = 4.dp
private val AddToCartMaxWidth = 270.dp
