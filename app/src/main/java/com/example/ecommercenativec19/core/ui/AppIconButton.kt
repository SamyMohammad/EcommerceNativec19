package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp

/**
 * Icon from the design (default 24dp) with a 48dp touch target.
 *
 * When clickable the layout size is therefore 48dp: outer margins that the design measures to the icon edge must be reduced by
 * [AppDimens.IconTouchInset] (12dp). Pass `onClick = null` for a purely decorative icon (then also pass
 * `contentDescription = null`); it keeps its plain icon size and no touch target.
 *
 * @param iconSize the icon box in the design.
 * @param imageSize the exported drawable size when it bleeds past the icon box (e.g. 26dp for the cart icon).
 */
@Composable
fun AppIconButton(
    painter: Painter,
    contentDescription: String?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    iconSize: Dp = AppDimens.IconSize,
    imageSize: Dp = iconSize,
) {
    val boxModifier = if (onClick != null) {
        modifier
            .minimumInteractiveComponentSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = AppDimens.MinTouchTarget / 2),
                role = Role.Button,
                onClick = onClick,
            )
    } else {
        modifier
    }
    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(imageSize),
        )
    }
}
