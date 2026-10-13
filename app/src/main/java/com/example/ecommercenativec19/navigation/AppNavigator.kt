package com.example.ecommercenativec19.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.ecommercenativec19.core.ui.AppTab

/**
 * App-owned navigation state: one auth stack (Splash, Sign in, Sign up) and one stack per bottom-bar tab.
 * Plain Kotlin on top of [MutableList]s, so it is unit-testable on the JVM; in the app the lists are
 * `NavBackStack`s, which are snapshot state and saved across configuration change and process death.
 *
 * While [authStack] is non-empty the user is signed out and only that stack is shown. Tab stacks are filled
 * lazily: a tab that was never opened holds no entries.
 */
class AppNavigator(
    private val authStack: MutableList<NavKey>,
    private val tabStacks: Map<AppTab, MutableList<NavKey>>,
    private val currentTabState: MutableState<AppTab>,
) {
    val currentTab: AppTab get() = currentTabState.value

    /** What [androidx.navigation3.ui.NavDisplay] renders; the last element is the visible screen. */
    val displayStack: List<NavKey>
        get() {
            if (authStack.isNotEmpty()) return authStack.toList()
            // Hidden tabs stay in the list (below the current one) so their entry-scoped ViewModels and
            // scroll state are not cleared when the user switches tabs.
            return AppTab.entries.filter { it != currentTab }.flatMap { tabStacks.getValue(it) } +
                tabStacks.getValue(currentTab)
        }

    val topKey: NavKey get() = displayStack.last()

    /** Splash finished: it removes itself and Sign in becomes the root. */
    fun finishSplash() {
        authStack.clear()
        authStack.add(SignIn)
    }

    /** Sign in / Sign up succeeded: drop the auth stack and land on the Home tab. */
    fun completeAuth() {
        authStack.clear()
        openTab(AppTab.Home)
    }

    /** Pushes [key] on the stack currently shown (the tab stack, or the auth stack while signed out). */
    fun push(key: NavKey) {
        activeStack().add(key)
    }

    /** Bottom bar tap. A different tab keeps its history; the current tab pops back to its root. */
    fun selectTab(tab: AppTab) {
        if (tab == currentTab) {
            val stack = tabStacks.getValue(tab)
            while (stack.size > 1) stack.removeAt(stack.lastIndex)
        } else {
            openTab(tab)
        }
    }

    /**
     * System back. Pops within the current stack; a non-Home tab root goes to the Home tab.
     * Returns false when nothing is left to pop and the caller should leave the app.
     */
    fun back(): Boolean {
        val stack = activeStack()
        return when {
            stack.size > 1 -> {
                stack.removeAt(stack.lastIndex)
                true
            }
            authStack.isNotEmpty() -> false
            currentTab != AppTab.Home -> {
                currentTabState.value = AppTab.Home
                true
            }
            else -> false
        }
    }

    private fun openTab(tab: AppTab) {
        val stack = tabStacks.getValue(tab)
        if (stack.isEmpty()) stack.add(tab.rootKey)
        currentTabState.value = tab
    }

    private fun activeStack(): MutableList<NavKey> =
        if (authStack.isNotEmpty()) authStack else tabStacks.getValue(currentTab)
}

/** Creates the navigator with saveable stacks. Start state: Splash. */
@Composable
fun rememberAppNavigator(): AppNavigator {
    val authStack = rememberNavBackStack(Splash)
    val homeStack = rememberNavBackStack(Home)
    val categoriesStack = rememberNavBackStack()
    val wishlistStack = rememberNavBackStack()
    val accountStack = rememberNavBackStack()
    val tab = rememberSaveable { mutableStateOf(AppTab.Home) }
    return remember(authStack, homeStack, categoriesStack, wishlistStack, accountStack, tab) {
        AppNavigator(
            authStack = authStack,
            tabStacks = mapOf(
                AppTab.Home to homeStack,
                AppTab.Categories to categoriesStack,
                AppTab.Wishlist to wishlistStack,
                AppTab.Account to accountStack,
            ),
            currentTabState = tab,
        )
    }
}
