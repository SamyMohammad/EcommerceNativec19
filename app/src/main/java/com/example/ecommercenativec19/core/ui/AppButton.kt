package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.ui.theme.extendedTypography

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

@Composable
@ReadOnlyComposable
private fun AppButtonStyle.container(): Color =
    if (this == AppButtonStyle.OnPrimary) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary

@Composable
@ReadOnlyComposable
private fun AppButtonStyle.content(): Color =
    if (this == AppButtonStyle.OnPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary

@Composable
@ReadOnlyComposable
private fun AppButtonStyle.shape(): CornerBasedShape = when (this) {
    AppButtonStyle.OnPrimary, AppButtonStyle.PrimaryCompact -> MaterialTheme.shapes.medium
    AppButtonStyle.Primary -> MaterialTheme.shapes.large
    AppButtonStyle.PrimarySmall -> MaterialTheme.shapes.small
}

@Composable
@ReadOnlyComposable
private fun AppButtonStyle.textStyle(): TextStyle = when (this) {
    AppButtonStyle.OnPrimary -> MaterialTheme.extendedTypography.authButton
    AppButtonStyle.Primary -> MaterialTheme.typography.titleLarge
    AppButtonStyle.PrimaryCompact -> MaterialTheme.typography.labelLarge
    AppButtonStyle.PrimarySmall -> MaterialTheme.typography.bodySmall
}

/**
 * Stateless button. The label is centered; [leadingIcon] / [trailingIcon] (24dp, decorative) are pinned to the
 * start/end edge at the style's icon inset, as in "Add to cart" (cart icon) and "Check Out" (arrow icon).
 * A disabled button (for example while a request is in flight) ignores taps and is dimmed.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AppButtonStyle = AppButtonStyle.Primary,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    enabled: Boolean = true,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(style.height).alpha(if (enabled) 1f else DisabledAlpha),
        shape = style.shape(),
        color = style.container(),
        contentColor = style.content(),
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = style.textStyle(),
                color = style.content(),
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

private const val DisabledAlpha = 0.6f
