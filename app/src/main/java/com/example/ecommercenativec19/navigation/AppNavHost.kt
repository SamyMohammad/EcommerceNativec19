package com.example.ecommercenativec19.navigation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommercenativec19.core.ui.AppTab
import com.example.ecommercenativec19.feature.account.AccountRoute
import com.example.ecommercenativec19.feature.cart.CartRoute
import com.example.ecommercenativec19.feature.categories.CategoriesRoute
import com.example.ecommercenativec19.feature.home.HomeRoute
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsRoute
import com.example.ecommercenativec19.feature.productlist.ProductListRoute
import com.example.ecommercenativec19.feature.signin.SignInRoute
import com.example.ecommercenativec19.feature.signup.SignUpRoute
import com.example.ecommercenativec19.feature.splash.SplashRoute
import com.example.ecommercenativec19.feature.wishlist.WishlistRoute

/**
 * Navigation graph of the ten screens. Start destination: [AppRoutes.Splash]. Every screen entry point is a
 * `XxxRoute(...)` composable in `feature/<name>/`; this file only wires the prototype transitions
 * (design/figma/source-evidence/interactions.json, restricted to these ten screens).
 *
 * ## Callback contract (what a screen may call, and where it leads)
 *
 * | Entry point | Callbacks | Prototype transition |
 * |---|---|---|
 * | `SplashRoute` | `onFinished()` | AFTER_TIMEOUT 0.4s -> Sign in (Splash is removed from the back stack) |
 * | `SignInRoute` | `onSignIn()`, `onCreateAccount()` | Login -> Home; "Create Account" -> Sign up |
 * | `SignUpRoute` | `onSignUp()` | Sign up button -> Home |
 * | `HomeRoute` | `onTabSelected(AppTab)`, `onOpenCart()`, `onOpenCategories()` | cart icon -> Cart; "men's fashion" circle -> Categories; tabs |
 * | `CategoriesRoute` | `onTabSelected(AppTab)`, `onOpenCart()`, `onOpenProductList()` | cart icon -> Cart; "Footwear" tile -> Product list; tabs |
 * | `ProductListRoute` | `onTabSelected(AppTab)`, `onOpenCart()`, `onOpenProduct(productId)` | cart icon -> Cart; product card -> Product details; tabs |
 * | `ProductDetailsRoute` | `productId`, `onBack()`, `onOpenCart()`, `onAddToCart()` | back arrow -> Product list; cart icon and "Add to cart" -> Cart |
 * | `CartRoute` | `onBack()` | back arrow -> Home |
 * | `WishlistRoute` | `onTabSelected(AppTab)`, `onOpenCart()` | cart icon -> Cart; tabs |
 * | `AccountRoute` | `onTabSelected(AppTab)` | tabs |
 *
 * `onTabSelected(tab)` is the bottom-bar contract: it navigates to `tab.route` as a top-level destination (pops
 * back to Home, single top, saved/restored state). The bar itself is `core.ui.BottomNavigationBar`; Product list
 * highlights [AppTab.Categories].
 *
 * Prototype reactions that are local UI state, not navigation, and therefore NOT routed here: favorite heart
 * toggles (Home, Product list, Details, Wishlist), quantity plus/minus (Details, Cart), the details image
 * carousel drag. Reactions whose destination is not one of the ten screens (Women's fashion -> 564:542,
 * Categories "Women's Fashion" tab -> 562:469) are intentionally not wired.
 *
 * ## Missing contracts (nothing is invented here)
 * - Sign in / Sign up submit only invoke the navigation lambda: there is no auth/backend contract in the design,
 *   no validation rules and no session state.
 * - "Check Out" (Cart), "Add to cart" side effects (Details), search and list data have no behavior in the
 *   prototype; the only documented effect of "Add to cart" is the transition to Cart.
 * - Product ids are opaque strings passed from Product list to Product details; there is no data layer yet.
 *
 * ## Transitions
 * The prototype uses SMART_ANIMATE (matching layers morph). Compose cannot reproduce that, so screens cross-fade:
 * Splash -> Sign in 400 ms linear; everything else 300 ms ease-out (durations/easings from interactions.json).
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppRoutes.Splash,
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { fadeIn(tween(ScreenFadeMillis, easing = EaseOut)) },
            exitTransition = { fadeOut(tween(ScreenFadeMillis, easing = EaseOut)) },
            popEnterTransition = { fadeIn(tween(ScreenFadeMillis, easing = EaseOut)) },
            popExitTransition = { fadeOut(tween(ScreenFadeMillis, easing = EaseOut)) },
        ) {
            composable(
                route = AppRoutes.Splash,
                exitTransition = { fadeOut(tween(SplashFadeMillis, easing = LinearEasing)) },
            ) {
                SplashRoute(
                    onFinished = {
                        navController.navigate(AppRoutes.SignIn) {
                            popUpTo(AppRoutes.Splash) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                route = AppRoutes.SignIn,
                enterTransition = { fadeIn(tween(SplashFadeMillis, easing = LinearEasing)) },
            ) {
                SignInRoute(
                    onSignIn = { navController.navigateToHomeAfterAuth() },
                    onCreateAccount = { navController.navigate(AppRoutes.SignUp) },
                )
            }
            composable(AppRoutes.SignUp) {
                SignUpRoute(onSignUp = { navController.navigateToHomeAfterAuth() })
            }
            composable(AppRoutes.Home) {
                HomeRoute(
                    onTabSelected = navController::navigateToTab,
                    onOpenCart = { navController.navigate(AppRoutes.Cart) },
                    onOpenCategories = { navController.navigateToTab(AppTab.Categories) },
                )
            }
            composable(AppRoutes.Categories) {
                CategoriesRoute(
                    onTabSelected = navController::navigateToTab,
                    onOpenCart = { navController.navigate(AppRoutes.Cart) },
                    onOpenProductList = { navController.navigate(AppRoutes.ProductList) },
                )
            }
            composable(AppRoutes.ProductList) {
                ProductListRoute(
                    onTabSelected = navController::navigateToTab,
                    onOpenCart = { navController.navigate(AppRoutes.Cart) },
                    onOpenProduct = { productId ->
                        navController.navigate(AppRoutes.productDetails(productId))
                    },
                )
            }
            composable(
                route = AppRoutes.ProductDetails,
                arguments = listOf(navArgument(AppRoutes.ARG_PRODUCT_ID) { type = NavType.StringType }),
            ) { entry: NavBackStackEntry ->
                ProductDetailsRoute(
                    productId = entry.arguments?.getString(AppRoutes.ARG_PRODUCT_ID).orEmpty(),
                    onBack = { navController.popBackStack() },
                    onOpenCart = { navController.navigate(AppRoutes.Cart) },
                    onAddToCart = { navController.navigate(AppRoutes.Cart) },
                )
            }
            composable(AppRoutes.Cart) {
                CartRoute(
                    onBack = {
                        // Prototype: the cart's back arrow always leads to Home.
                        navController.navigate(AppRoutes.Home) {
                            popUpTo(AppRoutes.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(AppRoutes.Wishlist) {
                WishlistRoute(
                    onTabSelected = navController::navigateToTab,
                    onOpenCart = { navController.navigate(AppRoutes.Cart) },
                )
            }
            composable(AppRoutes.Account) {
                AccountRoute(onTabSelected = navController::navigateToTab)
            }
        }
    }
}

/** Bottom-bar navigation: top-level destination, back stack collapsed to Home, state kept per tab. */
private fun NavController.navigateToTab(tab: AppTab) {
    navigate(tab.route) {
        popUpTo(AppRoutes.Home) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Sign in / Sign up lead to Home and drop the auth screens from the back stack. */
private fun NavController.navigateToHomeAfterAuth() {
    navigate(AppRoutes.Home) {
        popUpTo(AppRoutes.SignIn) { inclusive = true }
    }
}

private const val ScreenFadeMillis = 300
private const val SplashFadeMillis = 400

/** Figma EASE_OUT. */
private val EaseOut: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
