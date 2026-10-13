package com.example.ecommercenativec19.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavKey
import com.example.ecommercenativec19.core.ui.AppTab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppNavigatorTest {
    private fun signedOut() = navigator(auth = listOf(Splash))

    private fun signedIn() = navigator(auth = emptyList())

    private fun navigator(auth: List<NavKey>): AppNavigator {
        val tabStacks = AppTab.entries.associateWith { mutableStateListOf<NavKey>() }
        tabStacks.getValue(AppTab.Home).add(Home)
        return AppNavigator(
            authStack = mutableStateListOf<NavKey>().apply { addAll(auth) },
            tabStacks = tabStacks,
            currentTabState = mutableStateOf(AppTab.Home),
        )
    }

    @Test
    fun `starts on splash`() {
        assertEquals(listOf<NavKey>(Splash), signedOut().displayStack)
    }

    @Test
    fun `finishing splash replaces it with sign in`() {
        val nav = signedOut()
        nav.finishSplash()
        assertEquals(listOf<NavKey>(SignIn), nav.displayStack)
    }

    @Test
    fun `back at sign in root exits instead of returning to splash`() {
        val nav = signedOut()
        nav.finishSplash()
        assertFalse(nav.back())
    }

    @Test
    fun `back from sign up returns to sign in`() {
        val nav = signedOut()
        nav.finishSplash()
        nav.push(SignUp)
        assertTrue(nav.back())
        assertEquals(SignIn, nav.topKey)
    }

    @Test
    fun `auth success clears the auth stack and shows Home`() {
        val nav = signedOut()
        nav.finishSplash()
        nav.push(SignUp)
        nav.completeAuth()
        assertEquals(listOf<NavKey>(Home), nav.displayStack)
        assertEquals(AppTab.Home, nav.currentTab)
    }

    @Test
    fun `back at Home root after auth exits and never returns to auth`() {
        val nav = signedOut()
        nav.finishSplash()
        nav.completeAuth()
        assertFalse(nav.back())
    }

    @Test
    fun `selecting a tab shows its root`() {
        val nav = signedIn()
        nav.selectTab(AppTab.Wishlist)
        assertEquals(Wishlist, nav.topKey)
        assertEquals(AppTab.Wishlist, nav.currentTab)
    }

    @Test
    fun `switching tabs preserves each tab history`() {
        val nav = signedIn()
        nav.selectTab(AppTab.Categories)
        nav.push(ProductList)
        nav.push(ProductDetails("7"))

        nav.selectTab(AppTab.Home)
        nav.selectTab(AppTab.Categories)

        assertEquals(ProductDetails("7"), nav.topKey)
        assertTrue(nav.back())
        assertEquals(ProductList, nav.topKey)
    }

    @Test
    fun `hidden tab stacks stay in the display list below the current tab`() {
        val nav = signedIn()
        nav.selectTab(AppTab.Categories)
        nav.push(ProductList)
        nav.selectTab(AppTab.Home)

        assertEquals(listOf(Categories, ProductList, Home), nav.displayStack)
    }

    @Test
    fun `reselecting the current tab pops it to its root`() {
        val nav = signedIn()
        nav.selectTab(AppTab.Categories)
        nav.push(ProductList)
        nav.push(ProductDetails("7"))

        nav.selectTab(AppTab.Categories)

        assertEquals(Categories, nav.topKey)
    }

    @Test
    fun `back pops within the current tab`() {
        val nav = signedIn()
        nav.push(Cart)
        assertTrue(nav.back())
        assertEquals(Home, nav.topKey)
    }

    @Test
    fun `back at a non-Home tab root goes to the Home tab`() {
        val nav = signedIn()
        nav.selectTab(AppTab.Account)
        assertTrue(nav.back())
        assertEquals(AppTab.Home, nav.currentTab)
        assertEquals(Home, nav.topKey)
    }

    @Test
    fun `back at Home root signals exit`() {
        assertFalse(signedIn().back())
    }

    @Test
    fun `cart back pops to the screen that opened it`() {
        val nav = signedIn()
        nav.selectTab(AppTab.Categories)
        nav.push(ProductList)
        nav.push(ProductDetails("3"))
        nav.push(Cart)

        assertTrue(nav.back())

        assertEquals(ProductDetails("3"), nav.topKey)
        assertEquals(AppTab.Categories, nav.currentTab)
    }

    @Test
    fun `product details is pushed on the current tab stack`() {
        val nav = signedIn()
        nav.push(ProductDetails("9"))
        assertEquals(listOf<NavKey>(Home, ProductDetails("9")), nav.displayStack)
    }

    @Test
    fun `bottom bar shows on tab screens and Product list but not on details or cart`() {
        assertEquals(AppTab.Home, Home.bottomBarTab)
        assertEquals(AppTab.Categories, ProductList.bottomBarTab)
        assertEquals(null, ProductDetails("1").bottomBarTab)
        assertEquals(null, Cart.bottomBarTab)
        assertEquals(null, SignIn.bottomBarTab)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `product details rejects a blank id`() {
        ProductDetails(" ")
    }
}
