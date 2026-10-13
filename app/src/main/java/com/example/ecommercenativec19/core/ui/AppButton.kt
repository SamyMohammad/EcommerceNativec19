package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.ui.theme.generated.AppTokens
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Button variants that appear in the design. Height, radius, colors and text style come from the style; the width
 * comes from the caller's modifier (398 wide on the auth screens, 270 for Add to cart / Check Out, 100 for the
 * wishlist row, 97 for "Shop Now").
 */
enum class AppButtonStyle(
    internal val height: Dp,
    /** Optional inset of [AppButton] leading/trailing icons from the button edge. */
    internal val iconInset: Dp,
) {
    /** White button on the primary background: "Login", "Sign up". 64dp, radius 15, SemiBold 20, primary text. */
    OnPrimary(64.dp, 0.dp),

    /** Primary-color button: "Add to cart", "Check Out". 48dp, radius 20, Medium 20, white text. */
    Primary(48.dp, 32.dp),

    /** Primary button inside list rows: wishlist "Add to Cart". 36dp, radius 15, Medium 14. */
    PrimaryCompact(36.dp, 0.dp),

    /** Primary button on banners: "Shop Now". 30dp, radius 10, Regular 12. */
    PrimarySmall(30.dp, 0.dp),
}

private fun AppButtonStyle.container(t: AppTokens): Color = if (this == AppButtonStyle.OnPrimary) t.colorSource02 else t.colorSource00
private fun AppButtonStyle.content(t: AppTokens): Color = if (this == AppButtonStyle.OnPrimary) t.colorSource00 else t.colorSource02
private fun AppButtonStyle.radius(t: AppTokens): Dp = when (this) {
    AppButtonStyle.OnPrimary, AppButtonStyle.PrimaryCompact -> t.radiusSource02
    AppButtonStyle.Primary -> t.radiusSource00
    AppButtonStyle.PrimarySmall -> t.radiusSource05
}
private fun AppButtonStyle.textStyle(t: AppTokens): TextStyle = when (this) {
    AppButtonStyle.OnPrimary -> t.typographySource02
    AppButtonStyle.Primary -> t.typographySource10
    AppButtonStyle.PrimaryCompact -> t.typographySource15
    AppButtonStyle.PrimarySmall -> t.typographySource11
}

/**
 * Stateless button. The label is centered; [leadingIcon] / [trailingIcon] (24dp, decorative) are pinned to the
 * start/end edge at the style's icon inset, as in "Add to cart" (cart icon) and "Check Out" (arrow icon).
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AppButtonStyle = AppButtonStyle.Primary,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
) {
    val tokens = LocalAppTokens.current
    Surface(
        onClick = onClick,
        modifier = modifier.height(style.height),
        shape = RoundedCornerShape(style.radius(tokens)),
        color = style.container(tokens),
        contentColor = style.content(tokens),
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = style.textStyle(tokens),
                color = style.content(tokens),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = if (leadingIcon != null || trailingIcon != null) {
                    Modifier.padding(horizontal = style.iconInset + AppDimens.IconSize)
                } else {
                    Modifier
                },
            )
            if (leadingIcon != null) {
                Image(
                    painter = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = style.iconInset)
                        .size(AppDimens.IconSize),
                )
            }
            if (trailingIcon != null) {
                Image(
                    painter = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = style.iconInset)
                        .size(AppDimens.IconSize),
                )
            }
        }
    }
}
