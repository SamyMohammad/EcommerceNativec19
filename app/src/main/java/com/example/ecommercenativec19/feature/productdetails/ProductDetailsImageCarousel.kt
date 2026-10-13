package com.example.ecommercenativec19.feature.productdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.CoverImage
import com.example.ecommercenativec19.core.ui.FavoriteButton

/**
 * Bordered 300dp image carousel with the favorite circle (top-end) and page indicators (bottom-center): the active
 * page is a 30 x 7 primary pill, the others are 7dp white dots with a primary outline. [currentPage] seeds the
 * pager; swipes are reported through [onPageChanged].
 */
@Composable
internal fun ProductDetailsImageCarousel(
    images: List<Int>,
    currentPage: Int,
    onPageChanged: (Int) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.medium
    val pagerState = rememberPagerState(initialPage = currentPage.coerceIn(0, images.lastIndex)) { images.size }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect(onPageChanged)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(CarouselHeight)
            .clip(shape)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape),
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            CoverImage(
                painter = painterResource(images[page]),
                contentDescription = stringResource(R.string.details_image_description, page + 1, images.size),
                modifier = Modifier.fillMaxSize(),
            )
        }
        FavoriteButton(
            isFavorite = isFavorite,
            onClick = onFavoriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = FavoriteTopInset, end = FavoriteEndInset),
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = IndicatorStartBias, bottom = IndicatorBottomInset),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(images.size) { index ->
                if (index > 0) {
                    val nextToActive = index == pagerState.currentPage || index - 1 == pagerState.currentPage
                    Spacer(Modifier.width(if (nextToActive) ActiveIndicatorGap else IndicatorGap))
                }
                if (index == pagerState.currentPage) {
                    Box(
                        Modifier
                            .size(width = ActiveIndicatorWidth, height = IndicatorSize)
                            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.extraLarge),
                    )
                } else {
                    Box(
                        Modifier
                            .width(IndicatorSize)
                            .height(IndicatorSize)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    )
                }
            }
        }
    }
}

private val CarouselHeight = 300.dp

/** The 30dp heart sits at x 351 / y 15 inside the 1dp border: 16dp from the top and the end of the outer box. */
private val FavoriteTopInset = 16.dp
private val FavoriteEndInset = 16.dp
private val IndicatorBottomInset = 8.dp

/** The design's indicator group starts at x 171 (3 pages: 171..231), 1dp right of centered in the 398dp box. */
private val IndicatorStartBias = 2.dp
private val IndicatorSize = 7.dp
private val ActiveIndicatorWidth = 30.dp

/** The design spaces the active pill from its neighbors by 10dp and the inactive dots by 6dp. */
private val ActiveIndicatorGap = 10.dp
private val IndicatorGap = 6.dp
