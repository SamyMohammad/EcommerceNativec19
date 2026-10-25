package com.example.ecommercenativec19.feature.cart

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.cart.domain.CartLine
import com.example.ecommercenativec19.feature.cart.domain.ObserveCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.RefreshCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.RemoveFromCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.UpdateCartItemCountUseCase
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
class CartViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var cart: FakeCartRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cart = FakeCartRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = CartViewModel(
        ObserveCartUseCase(cart),
        RefreshCartUseCase(cart),
        UpdateCartItemCountUseCase(cart),
        RemoveFromCartUseCase(cart),
    )

    /** `state` is a WhileSubscribed StateFlow, so something must collect it, as the Route does. */
    private fun TestScope.subscribed(viewModel: CartViewModel): CartViewModel {
        backgroundScope.launch { viewModel.state.collect {} }
        return viewModel
    }

    private fun line(id: String, count: Int = 1, unitPrice: Int = 100) = CartLine(id, "Product $id", "img-$id", unitPrice, count)

    private fun CartUiState.rows(): List<CartLineUi> = (items as UiState.Success<List<CartLineUi>>).data

    @Test
    fun `starts Loading while no cart is cached`() = runTest(testDispatcher) {
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        assertEquals(UiState.Loading, viewModel.state.value.items)
    }

    @Test
    fun `a cached cart shows its rows with the server total`() = runTest(testDispatcher) {
        cart.setCart(line("a", count = 2, unitPrice = 100), line("b", unitPrice = 50), totalPrice = 240)
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf("a", "b"), state.rows().map { it.id })
        assertEquals(200, state.rows().first().linePrice)
        assertEquals(240, state.totalPrice)
    }

    @Test
    fun `a cart without lines is Empty`() = runTest(testDispatcher) {
        cart.setCart()
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        assertEquals(UiState.Empty, viewModel.state.value.items)
    }

    @Test
    fun `a failed first load shows the error and retry reloads`() = runTest(testDispatcher) {
        cart.refreshResult = AppResult.Failure(AppError.Network)
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()
        assertEquals(UiState.Error(AppError.Network), viewModel.state.value.items)

        cart.refreshResult = AppResult.Success(Unit)
        cart.setCart(line("a"))
        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(listOf("a"), viewModel.state.value.rows().map { it.id })
        assertEquals(2, cart.refreshCalls)
    }

    @Test
    fun `a failed refresh keeps the cached rows`() = runTest(testDispatcher) {
        cart.setCart(line("a"))
        cart.refreshResult = AppResult.Failure(AppError.Network)
        val viewModel = subscribed(viewModel())
        viewModel.onEnter()
        advanceUntilIdle()

        assertEquals(listOf("a"), viewModel.state.value.rows().map { it.id })
    }

    @Test
    fun `increment asks for one more than the cached count`() = runTest(testDispatcher) {
        cart.setCart(line("a", count = 2))
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        viewModel.onIncrement("a")
        advanceUntilIdle()

        assertEquals(listOf("a" to 3), cart.updateCalls)
        assertEquals(3, viewModel.state.value.rows().single().quantity)
    }

    @Test
    fun `decrement asks for one less`() = runTest(testDispatcher) {
        cart.setCart(line("a", count = 3))
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        viewModel.onDecrement("a")
        advanceUntilIdle()

        assertEquals(listOf("a" to 2), cart.updateCalls)
    }

    @Test
    fun `decrementing the last unit removes the line`() = runTest(testDispatcher) {
        cart.setCart(line("a", count = 1))
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        viewModel.onDecrement("a")
        advanceUntilIdle()

        assertEquals(listOf("a"), cart.removeCalls)
        assertEquals(emptyList<Pair<String, Int>>(), cart.updateCalls)
        assertEquals(UiState.Empty, viewModel.state.value.items)
    }

    @Test
    fun `remove deletes the line`() = runTest(testDispatcher) {
        cart.setCart(line("a"), line("b"))
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        viewModel.onRemove("a")
        advanceUntilIdle()

        assertEquals(listOf("b"), viewModel.state.value.rows().map { it.id })
    }

    @Test
    fun `a failed change leaves the rows as they were`() = runTest(testDispatcher) {
        cart.setCart(line("a", count = 2))
        cart.mutationError = AppError.Network
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        viewModel.onIncrement("a")
        advanceUntilIdle()

        assertEquals(2, viewModel.state.value.rows().single().quantity)
    }

    @Test
    fun `signing out sends the screen back to Loading`() = runTest(testDispatcher) {
        cart.setCart(line("a"))
        val viewModel = subscribed(viewModel())
        advanceUntilIdle()

        cart.clear()
        advanceUntilIdle()

        assertEquals(UiState.Loading, viewModel.state.value.items)
    }
}
