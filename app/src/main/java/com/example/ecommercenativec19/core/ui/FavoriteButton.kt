package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R

/** Diameter of the white circle ("Group 17") in the design. */
enum class FavoriteButtonSize(internal val node: Dp) {
    /** 16dp, on the Home product cards. */
    Small(16.dp),

    /** 30dp, on Product list / Product details / Wishlist. */
    Regular(30.dp),
}

/** The design's drop shadow on the circle: offset y 5, Gaussian sigma 5, 15% black (at the 30dp node scale). */
private const val ShadowOffsetY = 5f
private const val ShadowSigma = 5f
private const val ShadowAlpha = 0.15f

/** Android's `Paint.setShadowLayer` takes a blur radius; Skia converts it to a sigma as `0.57735 * radius + 0.5`. */
private fun shadowRadiusForSigma(sigma: Float): Float = ((sigma - 0.5f) / 0.57735f).coerceAtLeast(0.01f)

/**
 * White circle with a heart (outline when not favorite, filled when favorite) overlaying product images.
 *
 * The exported SVGs carry a soft drop shadow (offset y 5, sigma 5, 15% black) that a VectorDrawable cannot
 * express, so it is drawn behind the button with a shadow layer (API 28+; older devices simply show no shadow).
 * Known gap: the filled variant exists only at the Regular scale, so [FavoriteButtonSize.Small] + favorite scales
 * it down by 16/30. The hit area is the circle (no 48dp
 * expansion, so it does not move the design's overlay position).
 */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: FavoriteButtonSize = FavoriteButtonSize.Regular,
) {
    val smallOutline = size == FavoriteButtonSize.Small && !isFavorite
    val drawable = when {
        smallOutline -> R.drawable.ic_group17_515793da
        isFavorite -> R.drawable.ic_property1variant2_cdba9644
        else -> R.drawable.ic_property1default_f0cffe88
    }
    // Exported canvas = node + shadow room: 36dp for the 16dp home button, 50dp for the 30dp one (offset 10 / 5).
    val scale = size.node.value / 30f
    val canvas = if (smallOutline) 36.dp else 50.dp * scale
    val bleedX = if (smallOutline) 10.dp else 10.dp * scale
    val bleedY = if (smallOutline) 5.dp else 5.dp * scale
    val description = stringResource(if (isFavorite) R.string.core_favorite_remove else R.string.core_favorite_add)
    Box(
        modifier = modifier
            .size(size.node)
            .drawBehind {
                val shadowScale = if (smallOutline) 1f else scale
                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                        color = Color.White.toArgb()
                        setShadowLayer(
                            shadowRadiusForSigma(ShadowSigma * shadowScale * density),
                            0f,
                            ShadowOffsetY * shadowScale * density,
                            Color.Black.copy(alpha = ShadowAlpha).toArgb(),
                        )
                    }
                    canvas.nativeCanvas.drawCircle(this.size.width / 2f, this.size.height / 2f, this.size.width / 2f, paint)
                }
            }
            .semantics { contentDescription = description }
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart, unbounded = true)
                .offset(x = -bleedX, y = -bleedY)
                .size(canvas),
        )
    }
}
