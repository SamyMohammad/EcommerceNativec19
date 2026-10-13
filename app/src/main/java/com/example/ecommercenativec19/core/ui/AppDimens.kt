package com.example.ecommercenativec19.core.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Layout measurements that the Figma design repeats on several screens but that have no design token
 * (the generated theme only carries colors, typography and radii). Values are logical px of the 430 x 932
 * reference frames, i.e. dp.
 */
object AppDimens {
    /** Left/right margin of screen content (x = 16 on every screen). */
    val ScreenHorizontal: Dp = 16.dp

    /** Standard icon box (search, cart, back, edit, delete ...). */
    val IconSize: Dp = 24.dp

    /** Minimum touch target; icons are 24dp so the target adds [IconTouchInset] on every side. */
    val MinTouchTarget: Dp = 48.dp

    /** Extra space a [MinTouchTarget] adds around an [IconSize] icon. Subtract it from outer margins. */
    val IconTouchInset: Dp = (MinTouchTarget - IconSize) / 2

    /** Height of the primary-colored bottom navigation bar (below it the system navigation bar inset). */
    val BottomBarHeight: Dp = 56.dp

    /** Height of the status bar band at the top of every design frame (content starts at or below y = 44). */
    val StatusBarDesignHeight: Dp = 44.dp

    /** Distance from the bottom of the (iOS) status bar to the top of the logo / title row. */
    val HeaderTopOffset: Dp = 6.dp

    /** Auth text field height (sign in / sign up). */
    val AuthFieldHeight: Dp = 64.dp

    /** Home-style search bar height. */
    val SearchBarHeight: Dp = 50.dp
}
