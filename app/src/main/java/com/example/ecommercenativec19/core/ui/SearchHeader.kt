package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Top-left logo row shared by Home, Categories, Product list, Wishlist and Account: below the status bar inset,
 * [AppDimens.HeaderTopOffset] down, 16dp from the start edge.
 */
@Composable
fun AppLogoBar(modifier: Modifier = Modifier, start: Dp = AppDimens.ScreenHorizontal) {
    Box(
        modifier = modifier
            .designStatusBarPadding()
            .padding(start = start, top = AppDimens.HeaderTopOffset),
    ) {
        AppLogo(style = AppLogoStyle.Header)
    }
}

/**
 * Logo + search field + cart icon, the header of Home, Categories, Product list and Wishlist.
 *
 * The search field is a real single-line text field (the design only shows the placeholder state);
 * the prototype defines no search behavior, so [onSearch] is optional and nothing else is implied.
 */
@Composable
fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (() -> Unit)? = null,
    logoToSearchGap: Dp = LogoToSearchGap,
    iconStart: Dp = SearchIconStart,
    iconToText: Dp = SearchIconToText,
    horizontalInset: Dp = AppDimens.ScreenHorizontal,
    searchToCartGap: Dp = SearchToCartGap,
) {
    Column(modifier) {
        AppLogoBar(start = horizontalInset)
        Spacer(Modifier.height(logoToSearchGap))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = horizontalInset,
                    end = horizontalInset - AppDimens.IconTouchInset,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SearchField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                iconStart = iconStart,
                iconToText = iconToText,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(searchToCartGap - AppDimens.IconTouchInset))
            AppIconButton(
                painter = painterResource(R.drawable.ic_icon_shopping_cart_26995765),
                contentDescription = stringResource(R.string.core_cart),
                onClick = onCartClick,
                imageSize = CartImageSize,
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (() -> Unit)?,
    iconStart: Dp,
    iconToText: Dp,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalAppTokens.current
    val shape = RoundedCornerShape(tokens.radiusSource06)
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(AppDimens.SearchBarHeight)
            .background(tokens.colorSource20, shape)
            .border(1.dp, tokens.colorSource00, shape),
        textStyle = tokens.typographySource14.copy(color = tokens.colorSource12),
        cursorBrush = SolidColor(tokens.colorSource00),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke() }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppDimens.SearchBarHeight)
                    .padding(start = iconStart, end = AppDimens.ScreenHorizontal),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_icon_search_e2303111),
                    contentDescription = stringResource(R.string.core_search),
                    modifier = Modifier.size(SearchImageSize),
                )
                Spacer(Modifier.width(iconToText))
                Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.core_search_placeholder),
                            style = tokens.typographySource14,
                            color = tokens.colorSource21,
                            maxLines = 1,
                            modifier = Modifier.textLineBox(tokens.typographySource14),
                        )
                    }
                    innerTextField()
                }
            }
        },
    )
}

/** Logo bottom (y 72) to search field top (y 91). */
internal val LogoToSearchGap = 19.dp

/** Search field right edge (x 364) to cart icon left edge (x 390). */
private val SearchToCartGap = 26.dp

/** Exported search / cart drawables include their stroke overflow around the 24dp icon box. */
private val SearchImageSize = 25.5.dp
private val CartImageSize = 26.dp

/**
 * Search icon left edge from the field's outer edge (CSS `6.92% - 0.86` of the 346dp padding box + 1dp border -
 * 0.75dp stroke overflow = 23.33dp), and the rendered icon's right edge (26px) to the placeholder start (the text
 * is centered on x 137 of the padding box: start = 72.13 - 16 - 23 - 26 = 7.13).
 */
internal val SearchIconStart = 23.33.dp
internal val SearchIconToText = 7.13.dp
