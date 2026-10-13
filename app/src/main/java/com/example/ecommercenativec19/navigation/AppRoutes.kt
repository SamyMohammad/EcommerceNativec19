package com.example.ecommercenativec19.navigation

import androidx.navigation3.runtime.NavKey
import com.example.ecommercenativec19.core.ui.AppTab
import kotlinx.serialization.Serializable

// Navigation 3 keys of the ten screens (Figma node ids in the comments, from interactions.json). Keys are plain
// serializable data so the back stacks survive configuration change and process death.

/** 1:2 */
@Serializable data object Splash : NavKey

/** 14:47 */
@Serializable data object SignIn : NavKey

/** 9:97 */
@Serializable data object SignUp : NavKey

/** 14:116 */
@Serializable data object Home : NavKey

/** 601:775 */
@Serializable data object Categories : NavKey

/** 726:650; opened from a Categories sub-category tile, so it lives on the Categories tab stack. */
@Serializable data class ProductList(val subCategoryId: String) : NavKey {
    init {
        require(subCategoryId.isNotBlank()) { "subCategoryId must not be blank" }
    }
}

/** 620:1083 */
@Serializable data class ProductDetails(val productId: String) : NavKey {
    init {
        require(productId.isNotBlank()) { "productId must not be blank" }
    }
}

/** 604:890 */
@Serializable data object Cart : NavKey

/** 731:1294 */
@Serializable data object Wishlist : NavKey

/** 835:523 */
@Serializable data object Account : NavKey

/** Root key of a bottom-navigation tab. */
val AppTab.rootKey: NavKey
    get() = when (this) {
        AppTab.Home -> Home
        AppTab.Categories -> Categories
        AppTab.Wishlist -> Wishlist
        AppTab.Account -> Account
    }

/** Tab highlighted while this key is on top, or null when the screen shows no bottom bar. */
val NavKey.bottomBarTab: AppTab?
    get() = when (this) {
        Home -> AppTab.Home
        Categories -> AppTab.Categories
        is ProductList -> AppTab.Categories
        Wishlist -> AppTab.Wishlist
        Account -> AppTab.Account
        else -> null
    }
