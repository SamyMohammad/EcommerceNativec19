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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ecommercenativec19.core.ui.centeredBorder
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Stateless Product details: title bar, scrollable content (carousel, title/price, sold + rating + quantity,
 * description, size, color) and a pinned footer with the total price and "Add to cart".
 */
@Composable
fun ProductDetailsScreen(
    state: ProductDetailsUiState,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    onAddToCart: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSizeSelected: (String) -> Unit,
    onColorSelected: (ProductColor) -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
    onImagePageChanged: (Int) -> Unit,
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
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                // The carousel starts at y 90, 2dp inside the 48dp title bar band that ends at 92.
                .offset(y = -CarouselOverlap)
                .verticalScroll(rememberScrollState()),
        ) {
            ProductDetailsImageCarousel(
                images = state.images,
                currentPage = state.imagePage,
                onPageChanged = onImagePageChanged,
                isFavorite = state.isFavorite,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.padding(horizontal = AppDimens.ScreenHorizontal),
            )
            Spacer(Modifier.size(SectionGap + CarouselToTitleExtra))
            ProductDetailsContent(
                state = state,
                onSizeSelected = onSizeSelected,
                onColorSelected = onColorSelected,
                onIncrementQuantity = onIncrementQuantity,
                onDecrementQuantity = onDecrementQuantity,
            )
            Spacer(Modifier.size(SectionGap))
        }
        ProductDetailsFooter(totalPrice = state.totalPrice, onAddToCart = onAddToCart)
    }
}

@Composable
private fun ProductDetailsContent(
    state: ProductDetailsUiState,
    onSizeSelected: (String) -> Unit,
    onColorSelected: (ProductColor) -> Unit,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
) {
    val tokens = LocalAppTokens.current
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimens.ScreenHorizontal),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = state.title,
                style = tokens.typographySource00,
                color = tokens.colorSource12,
                modifier = Modifier.weight(1f).textLineBox(tokens.typographySource00),
            )
            Spacer(Modifier.width(SectionGap))
            Text(
                text = state.price,
                style = tokens.typographySource00,
                color = tokens.colorSource12,
                modifier = Modifier.textLineBox(tokens.typographySource00),
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
            val pillShape = RoundedCornerShape(tokens.radiusSource00)
            Box(
                modifier = Modifier
                    .heightIn(min = SoldPillHeight)
                    // The sold pill's stroke is centered on its edge in the design (half a dp outside the box).
                    .centeredBorder(1.dp, tokens.colorSource14, pillShape)
                    .padding(horizontal = SoldPillHorizontalPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.soldLabel,
                    style = tokens.typographySource15,
                    color = tokens.colorSource12,
                    modifier = Modifier.textLineBox(tokens.typographySource15),
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
                    text = state.ratingLabel,
                    style = tokens.typographySource07,
                    color = tokens.colorSource12,
                    modifier = Modifier.textLineBox(tokens.typographySource07),
                )
            }
            QuantitySelector(
                quantity = state.quantity,
                onIncrement = onIncrementQuantity,
                onDecrement = onDecrementQuantity,
            )
        }
        Spacer(Modifier.size(HeadingGap))
        SectionHeading(stringResource(R.string.details_description_heading))
        Spacer(Modifier.size(HeadingGap))
        Text(
            text = buildAnnotatedString {
                append(state.description)
                withStyle(SpanStyle(color = tokens.colorSource12)) { append(stringResource(R.string.details_read_more)) }
            },
            style = tokens.typographySource07,
            color = tokens.colorSource21,
            // The multi-line text keeps the font's natural first-line ascent (a line box of 18dp cannot be forced on
            // it), so it is drawn 1dp up and the layout gap below it is 2dp shorter to land on the design's y.
            modifier = Modifier
                .offset(y = -DescriptionRaise)
                .padding(start = AppDimens.ScreenHorizontal, end = DescriptionEndPadding),
        )
        Spacer(Modifier.size(SectionGap - DescriptionExtraHeight))
        SectionHeading(stringResource(R.string.details_size_heading))
        Spacer(Modifier.size(OptionsTopGap))
        ProductSizeOptions(state.sizes, state.selectedSize, onSizeSelected)
        Spacer(Modifier.size(SectionGap - OptionsInset))
        SectionHeading(stringResource(R.string.details_color_heading))
        Spacer(Modifier.size(OptionsTopGap))
        ProductColorOptions(state.colors, state.selectedColor, onColorSelected)
    }
}

@Composable
private fun SectionHeading(text: String) {
    val tokens = LocalAppTokens.current
    Text(
        text = text,
        style = tokens.typographySource00,
        color = tokens.colorSource12,
        modifier = Modifier.textLineBox(tokens.typographySource00).padding(horizontal = AppDimens.ScreenHorizontal),
    )
}

@Composable
private fun ProductDetailsFooter(totalPrice: String, onAddToCart: () -> Unit) {
    val tokens = LocalAppTokens.current
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
                style = tokens.typographySource00,
                color = tokens.colorSource21,
                modifier = Modifier.textLineBox(tokens.typographySource00),
            )
            Text(
                text = totalPrice,
                style = tokens.typographySource00,
                color = tokens.colorSource12,
                modifier = Modifier.textLineBox(tokens.typographySource00),
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
private val DescriptionExtraHeight = 2.dp

/** The 35dp option circles sit centered in 48dp-tall targets: 6.5dp of the 8dp heading gap is inside the target. */
private val OptionsInset = 6.5.dp
private val OptionsTopGap = 1.dp

/** Carousel top (y 90) is above the title bar band's bottom (y 92). */
private val CarouselOverlap = 2.dp
private val SoldPillHeight = 34.dp
private val SoldPillHorizontalPadding = 16.dp
private val SoldToRatingGap = 16.dp
private val StarSize = 15.dp
private val StarGap = 4.dp
private val AddToCartMaxWidth = 270.dp
