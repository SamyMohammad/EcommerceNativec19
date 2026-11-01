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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.core.ui.AppTab
import com.example.ecommercenativec19.feature.account.AccountRoute
import com.example.ecommercenativec19.feature.auth.domain.ObserveSessionUseCase
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
 * Navigation root of the ten screens. Start destination: [Splash]. Maps every key to its `XxxRoute(...)`
 * composable and turns screen callbacks into [AppNavigator] calls; back stacks live in [AppNavigator].
 * Callback contract, prototype transitions and what is intentionally not wired: docs/architecture.md section 6.
 */
@Composable
fun AppNavDisplay(
    observeSession: ObserveSessionUseCase,
    modifier: Modifier = Modifier,
    navigator: AppNavigator = rememberAppNavigator(),
) {
    val activity = LocalActivity.current
    val sessionState by observeSession().collectAsStateWithLifecycle()
    // Only react once past the auth screens: Splash and Sign in are already the signed-out UI.
    LaunchedEffect(sessionState) {
        if (sessionState == SessionState.SignedOut && navigator.isSignedIn) navigator.signOut()
    }
    AppScaffold(
        selectedTab = navigator.topKey.bottomBarTab,
        onTabSelected = navigator::selectTab,
        modifier = modifier,
    ) {
        NavDisplay(
            backStack = navigator.displayStack,
            onBack = { if (!navigator.back()) activity?.finish() },
            // Per-entry ViewModelStore: hiltViewModel() resolves LocalViewModelStoreOwner to the entry.
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            transitionSpec = { fade(ScreenFadeMillis, EaseOut) },
            popTransitionSpec = { fade(ScreenFadeMillis, EaseOut) },
            predictivePopTransitionSpec = { fade(ScreenFadeMillis, EaseOut) },
            entryProvider = entryProvider<NavKey> {
                authEntries(navigator)
                mainEntries(navigator)
            },
        )
    }
}

/** Signed-out screens: Splash, Sign in, Sign up. */
private fun EntryProviderScope<NavKey>.authEntries(navigator: AppNavigator) {
    entry<Splash> {
        SplashRoute(onSignedIn = navigator::completeAuth, onSignedOut = navigator::finishSplash)
    }
    entry<SignIn>(
        metadata = NavDisplay.transitionSpec { fade(SplashFadeMillis, LinearEasing) },
    ) {
        SignInRoute(onSignedIn = navigator::completeAuth, onCreateAccount = { navigator.push(SignUp) })
    }
    entry<SignUp> { SignUpRoute(onSignedUp = navigator::completeAuth) }
}

/** Signed-in screens: the four tab roots and the screens pushed on top of them. */
private fun EntryProviderScope<NavKey>.mainEntries(navigator: AppNavigator) {
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
    entry<Wishlist> {
        WishlistRoute(
            onOpenCart = { navigator.push(Cart) },
            onOpenProduct = { productId -> navigator.push(ProductDetails(productId)) },
        )
    }
    entry<Account> { AccountRoute() }
}

private fun fade(millis: Int, easing: Easing): ContentTransform =
    fadeIn(tween(millis, easing = easing)) togetherWith fadeOut(tween(millis, easing = easing))

private const val ScreenFadeMillis = 300
private const val SplashFadeMillis = 400

/** Figma EASE_OUT. */
private val EaseOut: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
