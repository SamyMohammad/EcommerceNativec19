package com.example.ecommercenativec19.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R

/**
 * The four bottom-navigation destinations of the design (Home, Categories, Wishlist, Account). Cart is not a tab:
 * it is opened from the cart icon in the headers. Each tab has a white icon (unselected) and a primary-colored
 * icon shown on a white circle (selected).
 */
enum class AppTab(
    @param:StringRes internal val label: Int,
    @param:DrawableRes internal val icon: Int,
    @param:DrawableRes internal val selectedIcon: Int,
) {
    Home(R.string.core_nav_home, R.drawable.ic_home13_7b03b532, R.drawable.ic_home13_9ab9e3b3),
    Categories(R.string.core_nav_categories, R.drawable.ic_category13_bc1cd3a4, R.drawable.ic_category13_5d9287a8),
    Wishlist(R.string.core_nav_wishlist, R.drawable.ic_heart33_e407d520, R.drawable.ic_heart33_19558981),
    Account(R.string.core_nav_account, R.drawable.ic_user13_dcfd1af0, R.drawable.ic_user13_b15673e0),
}

/**
 * Primary-colored 56dp bottom bar (radius 15) with the four [AppTab]s. The bar background extends under the
 * system navigation bar inset. Stateless: the caller owns [selectedTab] and reacts to [onTabSelected].
 *
 * Product list highlights [AppTab.Categories] (the design's selected circle is on the second tab there).
 */
@Composable
fun BottomNavigationBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.BottomBarHeight)
                .padding(horizontal = BarEdgePadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppTab.entries.forEach { tab ->
                BottomNavigationItem(
                    tab = tab,
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    tab: AppTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = AppDimens.MinTouchTarget / 2),
                role = Role.Tab,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(
                painter = painterResource(R.drawable.ic_ellipse39_5e07960d),
                contentDescription = null,
                modifier = Modifier.size(ItemSize),
            )
        }
        Image(
            painter = painterResource(if (selected) tab.selectedIcon else tab.icon),
            contentDescription = stringResource(tab.label),
            modifier = Modifier.size(AppDimens.IconSize),
        )
    }
}

/** Selected circle behind the icon (40 x 40). */
private val ItemSize = 40.dp

/**
 * Design item centers are x = 75, 168, 262, 355 (40dp items at 55, 148, 242, 335). With SpaceBetween, 48dp targets and
 * a 51dp edge margin the centers fall at 75, 168.3, 261.7, 355.
 */
private val BarEdgePadding = 51.dp
