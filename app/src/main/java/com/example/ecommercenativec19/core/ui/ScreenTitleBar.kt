package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R

/**
 * Title bar of Product details and Cart: back arrow (start), centered title (Medium 20), search and cart icons (end).
 * It starts at the design's status bar band (44dp, or the real inset when taller). [onSearchClick] / [onCartClick] may be null to hide the icon (the design
 * shows no behavior for the search icon).
 */
@Composable
fun ScreenTitleBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: (() -> Unit)? = null,
    onCartClick: (() -> Unit)? = null,
) {
    val edgePadding = AppDimens.ScreenHorizontal - AppDimens.IconTouchInset
    Box(
        modifier = modifier
            .fillMaxWidth()
            .designStatusBarPadding()
            .height(AppDimens.MinTouchTarget),
    ) {
        AppIconButton(
            painter = painterResource(R.drawable.ic_icon_arrow_back_outline_748fc219),
            contentDescription = stringResource(R.string.core_back),
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = edgePadding)
                .offset(y = -IconRaise),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = TitleSideReserve)
                .offset(y = -TitleRaise),
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = edgePadding)
                .offset(y = -IconRaise),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onSearchClick != null) {
                AppIconButton(
                    painter = painterResource(R.drawable.ic_icon_search_f2b4d17a),
                    contentDescription = stringResource(R.string.core_search),
                    onClick = onSearchClick,
                    imageSize = SearchImageSize,
                )
            }
            if (onSearchClick != null && onCartClick != null) {
                // Search (x 334) and cart (x 390) icons are 56 apart; the 48dp targets leave 8.
                Spacer(Modifier.width(8.dp))
            }
            if (onCartClick != null) {
                AppIconButton(
                    painter = painterResource(R.drawable.ic_icon_shopping_cart_26995765),
                    contentDescription = stringResource(R.string.core_cart),
                    onClick = onCartClick,
                    imageSize = CartImageSize,
                )
            }
        }
    }
}

/**
 * The design centers the icons at y 67 and the title text at y 65 while the 48dp bar spans 44..92 (center 68): raise
 * the icons by 1dp and the title by 3dp.
 */
private val IconRaise = 1.dp
private val TitleRaise = 3.dp

/** Keeps a long title clear of the back button and the two end icons. */
private val TitleSideReserve = 112.dp
private val SearchImageSize = 25.5.dp
private val CartImageSize = 26.dp
