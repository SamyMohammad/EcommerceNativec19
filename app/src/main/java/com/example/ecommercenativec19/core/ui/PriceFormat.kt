package com.example.ecommercenativec19.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.ecommercenativec19.R
import java.text.NumberFormat
import java.util.Locale

/**
 * Display formatting lives in the UI layer so state and domain keep plain numbers (whole EGP) that are easy to
 * test and independent of the device locale.
 */
@Composable
fun formatPrice(amount: Int): String = stringResource(R.string.core_price, formatAmount(amount))

/** The struck-through price of a discounted product, written "2,000 EGP" in the design. */
@Composable
fun formatOldPrice(amount: Int): String = stringResource(R.string.core_old_price, formatAmount(amount))

/** One decimal, like the design's "4.6". */
fun formatRating(rating: Double): String = String.format(Locale.getDefault(), "%.1f", rating)

/** Grouped integer, e.g. 3,500. */
@Composable
fun formatAmount(amount: Int): String {
    val locale = Locale.getDefault()
    return remember(amount, locale) { NumberFormat.getIntegerInstance(locale).format(amount) }
}
