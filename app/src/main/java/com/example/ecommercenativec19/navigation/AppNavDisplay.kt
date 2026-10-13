package com.example.ecommercenativec19.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
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
 * Navigation root of the ten screens. Start destination: [Splash]. Every screen entry point is a
 * `XxxRoute(...)` composable in `feature/<name>/`; this file only wires the prototype transitions
 * (design/figma/source-evidence/interactions.json, restricted to these ten screens). Back stacks live in
 * [AppNavigator]; see docs/architecture.md section 6.
 *
 * ## Callback contract (what a screen may call, and where it leads)
 *
 * | Entry point | Callbacks | Prototype transition |
 * |---|---|---|
 * | `SplashRoute` | `onFinished()` | AFTER_TIMEOUT 0.4s -> Sign in (Splash is removed from the back stack) |
 * | `SignInRoute` | `onSignIn()`, `onCreateAccount()` | Login -> Home; "Create Account" -> Sign up |
 * | `SignUpRoute` | `onSignUp()` | Sign up button -> Home |
 * | `HomeRoute` | `onOpenCart()`, `onOpenCategories()`, `onOpenProduct(productId)` | cart icon -> Cart; any category circle -> Categories; product card -> Product details |
 * | `CategoriesRoute` | `onOpenCart()`, `onOpenProductList(subCategoryId)` | cart icon -> Cart; any sub-category tile -> Product list of that sub-category |
 * | `ProductListRoute` | `subCategoryId`, `onOpenCart()`, `onOpenProduct(productId)` | cart icon -> Cart; product card -> Product details |
 * | `ProductDetailsRoute` | `productId`, `onBack()`, `onOpenCart()`, `onAddToCart()` | back arrow -> Product list; cart icon and "Add to cart" -> Cart |
 * | `CartRoute` | `onBack()` | back arrow -> previous screen (deviation: the prototype always leads to Home) |
 * | `WishlistRoute` | `onOpenCart()` | cart icon -> Cart |
 * | `AccountRoute` | none | - |
 *
 * The bottom bar is shared ([AppScaffold]) and calls [AppNavigator.selectTab]; Product list highlights the
 * Categories tab.
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
 * - Product and sub-category ids are opaque strings from the API.
 *
 * ## Transitions
 * The prototype uses SMART_ANIMATE (matching layers morph). Compose cannot reproduce that, so screens cross-fade:
 * Splash -> Sign in 400 ms linear; everything else 300 ms ease-out (durations/easings from interactions.json).
 */
@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
    navigator: AppNavigator = rememberAppNavigator(),
) {
    val activity = LocalActivity.current
    AppScaffold(
        selectedTab = navigator.topKey.bottomBarTab,
        onTabSelected = navigator::selectTab,
        modifier = modifier,
    ) {
        NavDisplay(
            backStack = navigator.displayStack,
            onBack = { if (!navigator.back()) activity?.finish() },
            // Per-entry ViewModelStore: koinViewModel() resolves LocalViewModelStoreOwner to the entry.
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            transitionSpec = { fade(ScreenFadeMillis, EaseOut) },
            popTransitionSpec = { fade(ScreenFadeMillis, EaseOut) },
            predictivePopTransitionSpec = { fade(ScreenFadeMillis, EaseOut) },
            entryProvider = entryProvider<NavKey> {
                entry<Splash> { SplashRoute(onFinished = navigator::finishSplash) }
                entry<SignIn>(
                    metadata = NavDisplay.transitionSpec { fade(SplashFadeMillis, LinearEasing) },
                ) {
                    SignInRoute(onSignIn = navigator::completeAuth, onCreateAccount = { navigator.push(SignUp) })
                }
                entry<SignUp> { SignUpRoute(onSignUp = navigator::completeAuth) }
                entry<Home> {
                    HomeRoute(
                        onOpenCart = { navigator.push(Cart) },
                        onOpenCategories = { navigator.selectTab(AppTab.Categories) },
                        onOpenProduct = { productId -> navigator.push(ProductDetails(productId)) },
                    )
                }
                entry<Categories> {
                    CategoriesRoute(
                        onOpenCart = { navigator.push(Cart) },
                        onOpenProductList = { subCategoryId -> navigator.push(ProductList(subCategoryId)) },
                    )
                }
                entry<ProductList> { key ->
                    ProductListRoute(
                        subCategoryId = key.subCategoryId,
                        onOpenCart = { navigator.push(Cart) },
                        onOpenProduct = { productId -> navigator.push(ProductDetails(productId)) },
                    )
                }
                entry<ProductDetails> { key ->
                    ProductDetailsRoute(
                        productId = key.productId,
                        onBack = { navigator.back() },
                        onOpenCart = { navigator.push(Cart) },
                        onAddToCart = { navigator.push(Cart) },
                    )
                }
                entry<Cart> { CartRoute(onBack = { navigator.back() }) }
                entry<Wishlist> { WishlistRoute(onOpenCart = { navigator.push(Cart) }) }
                entry<Account> { AccountRoute() }
            },
        )
    }
}

private fun fade(millis: Int, easing: Easing): ContentTransform =
    fadeIn(tween(millis, easing = easing)) togetherWith fadeOut(tween(millis, easing = easing))

private const val ScreenFadeMillis = 300
private const val SplashFadeMillis = 400

/** Figma EASE_OUT. */
private val EaseOut: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
