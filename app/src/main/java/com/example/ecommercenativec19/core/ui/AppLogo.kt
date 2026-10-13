package com.example.ecommercenativec19.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R

/**
 * The two placements of the "Route" word-mark in the design.
 *
 * The exported SVG is slightly larger than the layout node (the stroke is exported as visible overflow), so each
 * style carries the node size and the overflow ("bleed") that the design applies around it.
 */
enum class AppLogoStyle(
    @param:DrawableRes internal val drawable: Int,
    val nodeWidth: Dp,
    val nodeHeight: Dp,
    internal val imageWidth: Dp,
    internal val imageHeight: Dp,
    internal val bleedX: Dp,
    internal val bleedY: Dp,
) {
    /** Blue logo, 66 x 22, top-left of Home / Categories / Product list / Wishlist / Account. */
    Header(R.drawable.ic_group5_0dfa833c, 66.dp, 22.dp, 67.5.dp, 23.99.dp, 0.75.dp, 0.75.dp),

    /** White logo, 237 x 71.099, centered at the top of Sign in / Sign up (on the primary color). */
    Hero(R.drawable.ic_group5_33322d88, 237.dp, 71.099.dp, 239.77.dp, 74.54.dp, 1.385.dp, 1.386.dp),
}

/** Stateless "Route" logo. The layout size is the design node size ([AppLogoStyle.nodeWidth] x nodeHeight). */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    style: AppLogoStyle = AppLogoStyle.Header,
) {
    Box(modifier.size(style.nodeWidth, style.nodeHeight)) {
        Image(
            painter = painterResource(style.drawable),
            contentDescription = stringResource(R.string.core_logo_description),
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart, unbounded = true)
                .offset(x = -style.bleedX, y = -style.bleedY)
                .size(style.imageWidth, style.imageHeight),
        )
    }
}
