package com.example.ecommercenativec19.feature.wishlist

import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.cart.FakeCartRepository
import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi
import com.example.ecommercenativec19.feature.products.product
import com.example.ecommercenativec19.feature.wishlist.domain.GetWishlistUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var wishlist: FakeWishlistRepository
    private lateinit var cart: FakeCartRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        wishlist = FakeWishlistRepository()
        cart = FakeCartRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = WishlistViewModel(
        GetWishlistUseCase(wishlist),
        ToggleFavoriteUseCase(wishlist),
        ObserveFavoriteIdsUseCase(wishlist),
        AddToCartUseCase(cart),
    )

    /** `state` is a WhileSubscribed StateFlow, so something must collect it, as the Route does. */
    private fun TestScope.subscribed(viewModel: WishlistViewModel): WishlistViewModel {
        backgroundScope.launch { viewModel.state.collect {} }
        return viewModel
    }

    private fun WishlistUiState.ids(): List<String> = (items as UiState.Success<List<ProductCardUi>>).data.map { it.id }

    private fun givenWishlist(vararg ids: String) {
        wishlist.refreshResult = AppResult.Success(ids.map { product(it) })
    }

    @Test
    fun `starts Loading`() = runTest(testDispatcher) {
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        assertEquals(UiState.Loading, viewModel.state.value.items)
    }

    @Test
    fun `entering the screen shows the wishlist products`() = runTest(testDispatcher) {
        givenWishlist("a", "b")
        val viewModel = subscribed(viewModel())

        viewModel.onEnter()
        advanceUntilIdle()

        assertEquals(listOf("a", "b"), viewModel.state.value.ids())
    }

    @Test
    fun `an empty wishlist is Empty`() = runTest(testDispatcher) {
        val viewModel = subscribed(viewModel())

        viewModel.onEnter()
        advanceUntilIdle()

        assertEquals(UiState.Empty, viewModel.state.value.items)
    }

    @Test
    fun `a failed load shows the error and retry loads again`() = runTest(testDispatcher) {
        wishlist.refreshResult = AppResult.Failure(AppError.Network)
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()
        assertEquals(UiState.Error(AppError.Network), viewModel.state.value.items)

        givenWishlist("a")
        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(listOf("a"), viewModel.state.value.ids())
    }

    @Test
    fun `un-hearting a row removes it`() = runTest(testDispatcher) {
        givenWishlist("a", "b")
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        viewModel.onToggleFavorite("a")
        advanceUntilIdle()

        assertEquals(listOf("a"), wishlist.toggleCalls)
        assertEquals(listOf("b"), viewModel.state.value.ids())
    }

    @Test
    fun `removing the last row shows Empty`() = runTest(testDispatcher) {
        givenWishlist("a")
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        viewModel.onToggleFavorite("a")
        advanceUntilIdle()

        assertEquals(UiState.Empty, viewModel.state.value.items)
    }

    @Test
    fun `a failed removal brings the row back`() = runTest(testDispatcher) {
        givenWishlist("a", "b")
        wishlist.toggleError = AppError.Network
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        viewModel.onToggleFavorite("a")
        advanceUntilIdle()

        assertEquals(listOf("a", "b"), viewModel.state.value.ids())
    }

    @Test
    fun `a product unfavorited elsewhere disappears`() = runTest(testDispatcher) {
        givenWishlist("a", "b")
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        wishlist.setIds(setOf("b"))
        advanceUntilIdle()

        assertEquals(listOf("b"), viewModel.state.value.ids())
    }

    @Test
    fun `entering again picks up a favorite added meanwhile`() = runTest(testDispatcher) {
        givenWishlist("a")
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        givenWishlist("a", "c")
        viewModel.onEnter()
        advanceUntilIdle()

        assertEquals(listOf("a", "c"), viewModel.state.value.ids())
    }

    @Test
    fun `reloading on enter keeps the rows visible and survives a failure`() = runTest(testDispatcher) {
        givenWishlist("a")
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        wishlist.refreshResult = AppResult.Failure(AppError.Network)
        viewModel.onEnter()
        advanceUntilIdle()

        assertEquals(listOf("a"), viewModel.state.value.ids())
    }

    @Test
    fun `query change is stored`() = runTest(testDispatcher) {
        val viewModel = subscribed(viewModel())

        viewModel.onQueryChange("shoe")
        advanceUntilIdle()

        assertEquals("shoe", viewModel.state.value.query)
    }

    @Test
    fun `add to cart puts one unit of that product in the cart and keeps the row`() = runTest(testDispatcher) {
        givenWishlist("a", "b")
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        viewModel.onAddToCart("a")
        advanceUntilIdle()

        assertEquals(listOf("a" to 1), cart.addCalls)
        assertEquals(listOf("a", "b"), viewModel.state.value.ids())
    }
}
