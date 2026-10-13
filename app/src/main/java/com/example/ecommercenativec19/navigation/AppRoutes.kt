package com.example.ecommercenativec19.navigation

import android.net.Uri
import com.example.ecommercenativec19.core.ui.AppTab

/** Route strings of the ten screens (Figma node ids in the comments, from interactions.json). */
object AppRoutes {
    const val ARG_PRODUCT_ID = "productId"

    /** 1:2 */
    const val Splash = "splash"

    /** 14:47 */
    const val SignIn = "sign_in"

    /** 9:97 */
    const val SignUp = "sign_up"

    /** 14:116 */
    const val Home = "home"

    /** 601:775 */
    const val Categories = "categories"

    /** 726:650 */
    const val ProductList = "product_list"

    /** 620:1083; takes the product id as path argument. */
    const val ProductDetails = "product_details/{$ARG_PRODUCT_ID}"

    /** 604:890 */
    const val Cart = "cart"

    /** 731:1294 */
    const val Wishlist = "wishlist"

    /** 835:523 */
    const val Account = "account"

    fun productDetails(productId: String): String = "product_details/${Uri.encode(productId)}"
}

/** Route of the screen a bottom-navigation tab opens. */
val AppTab.route: String
    get() = when (this) {
        AppTab.Home -> AppRoutes.Home
        AppTab.Categories -> AppRoutes.Categories
        AppTab.Wishlist -> AppRoutes.Wishlist
        AppTab.Account -> AppRoutes.Account
    }
