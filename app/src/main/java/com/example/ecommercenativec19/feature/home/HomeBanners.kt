package com.example.ecommercenativec19.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.CoverImage
import com.example.ecommercenativec19.core.ui.FractionalCropImage
import com.example.ecommercenativec19.core.ui.LineBoxText
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Promo banner. The design shows one slide with a 3-dot indicator (first dot active) but no content for slides 2
 * and 3, so it is rendered as the single static slide plus the exported indicator artwork (no pager/carousel).
 *
 * The photo has the product on its right, so the banner keeps left-to-right layout in RTL locales (artwork-bound).
 */
@Composable
internal fun PromoBanner(modifier: Modifier = Modifier) {
    val tokens = LocalAppTokens.current
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = BannerMinHeight)
                .clip(RoundedCornerShape(tokens.radiusSource02)),
        ) {
            CoverImage(painterResource(R.drawable.img_unsplash_pdx_a82obo_dedce9f5), null, Modifier.matchParentSize())
            // Design-absolute layout: each line sits at its own y inside the 398 x 200 banner.
            Text(
                text = stringResource(R.string.home_banner_up_to),
                style = tokens.typographySource08,
                color = tokens.colorSource00,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(start = BannerTextStart, top = BannerUpToTop).textLineBox(tokens.typographySource08),
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(tokens.typographySource09.toSpanStyle().copy(color = tokens.colorSource00)) {
                        append(stringResource(R.string.home_banner_discount_value))
                    }
                    withStyle(tokens.typographySource10.toSpanStyle().copy(color = tokens.colorSource00)) {
                        append(stringResource(R.string.home_banner_discount_suffix))
                    }
                },
                style = tokens.typographySource09,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(start = BannerTextStart, top = BannerDiscountTop).textLineBox(tokens.typographySource09),
            )
            LineBoxText(
                text = stringResource(R.string.home_banner_description),
                style = tokens.typographySource07,
                color = tokens.colorSource00,
                modifier = Modifier.padding(start = BannerTextStart, top = BannerDescriptionTop),
            )
            // Visual only: the prototype defines no action for the banner's "Shop Now".
            Box(
                modifier = Modifier
                    .padding(start = BannerTextStart, top = ShopNowTop)
                    .heightIn(min = ShopNowHeight)
                    .widthIn(min = ShopNowWidth)
                    .background(tokens.colorSource00, RoundedCornerShape(tokens.radiusSource05))
                    .padding(horizontal = ShopNowPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.home_banner_cta),
                    style = tokens.typographySource07,
                    color = tokens.colorSource02,
                    modifier = Modifier.textLineBox(tokens.typographySource07),
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_group9_165d5c5e),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = DotsBottom)
                    .size(width = DotsWidth, height = DotsHeight),
            )
        }
    }
}

/**
 * Footwear promo (bg tint, brand icons, label, "up to 50% off" and the rotated sneaker photo). Artwork-bound, so it
 * keeps left-to-right layout in RTL locales.
 */
@Composable
internal fun FootwearBanner(modifier: Modifier = Modifier) {
    val tokens = LocalAppTokens.current
    val shape = RoundedCornerShape(tokens.radiusSource02)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FootwearHeight)
                .clip(shape)
                .background(tokens.colorSource18)
                .border(1.dp, tokens.colorSource14, shape),
        ) {
            FractionalCropImage(
                painter = painterResource(R.drawable.img_frame16_89c069cc),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd).size(width = FootwearArtWidth, height = FootwearHeight),
                widthFraction = 1.0158f,
                heightFraction = 1.999f,
                offsetXFraction = -0.0019f,
                offsetYFraction = -0.5198f,
            )
            FractionalCropImage(
                painter = painterResource(R.drawable.img_unsplash_h60pi_pe_h8iq_b9a6ad77),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = ShoeX, y = ShoeY)
                    .size(width = ShoeWidth, height = ShoeHeight)
                    .rotate(ShoeRotation),
                widthFraction = 1.5663f,
                heightFraction = 1.525f,
                offsetXFraction = -0.3719f,
                offsetYFraction = -0.2125f,
            )
            Column(
                modifier = Modifier
                    .widthIn(max = FootwearTextWidth)
                    .padding(start = FootwearTextStart, top = FootwearTextTop, bottom = FootwearTextTop),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(BrandIconGap)) {
                    Image(
                        painter = painterResource(R.drawable.ic_icon_brand_nike_10c0541a),
                        contentDescription = null,
                        modifier = Modifier.size(width = BrandIconWidth, height = BrandIconHeight),
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_icon_adidas_1e46bbca),
                        contentDescription = null,
                        modifier = Modifier.size(width = BrandIconWidth, height = BrandIconHeight),
                    )
                }
                Text(
                    text = stringResource(R.string.home_footwear_label),
                    style = tokens.typographySource07,
                    color = tokens.colorSource12,
                    modifier = Modifier.padding(top = FootwearLabelTop),
                )
                Text(
                    text = stringResource(R.string.home_footwear_offer),
                    style = tokens.typographySource12,
                    color = tokens.colorSource12,
                    modifier = Modifier.padding(top = FootwearOfferTop),
                )
            }
        }
    }
}

private val BannerMinHeight = 200.dp
private val BannerTextStart = 16.dp

/** Line tops inside the 398 x 200 banner (16.5%, 29.5%, 42.5% and 64.5% of its height). */
private val BannerUpToTop = 33.dp
private val BannerDiscountTop = 59.dp
private val BannerDescriptionTop = 85.dp
private val ShopNowTop = 129.dp
private val ShopNowWidth = 113.dp
private val ShopNowHeight = 38.dp
private val ShopNowPadding = 16.dp
private val DotsWidth = 46.dp
private val DotsHeight = 10.dp
private val DotsBottom = 8.dp

private val FootwearHeight = 110.dp
private val FootwearArtWidth = 143.dp
private val FootwearTextStart = 23.dp
private val FootwearTextTop = 21.dp
private val FootwearTextWidth = 153.dp
private val FootwearLabelTop = 13.dp
private val FootwearOfferTop = 6.dp
private val BrandIconWidth = 24.dp
private val BrandIconHeight = 10.dp
private val BrandIconGap = 4.dp

/** Rotated sneaker photo: 93.249 x 120.321 box, rotated -28.71 degrees, top-left at (164.02, 0.63). */
private val ShoeX = 164.02.dp
private val ShoeY = 0.63.dp
private val ShoeWidth = 93.249.dp
private val ShoeHeight = 120.321.dp
private const val ShoeRotation = -28.71f
