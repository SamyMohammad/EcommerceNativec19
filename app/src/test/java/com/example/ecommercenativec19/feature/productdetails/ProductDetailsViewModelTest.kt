package com.example.ecommercenativec19.feature.productdetails

import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.cart.FakeCartRepository
import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.products.FakeProductsRepository
import com.example.ecommercenativec19.feature.products.domain.GetProductDetailsUseCase
import com.example.ecommercenativec19.feature.products.product
import com.example.ecommercenativec19.feature.wishlist.FakeWishlistRepository
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeProductsRepository
    private lateinit var wishlist: FakeWishlistRepository
    private lateinit var cart: FakeCartRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeProductsRepository()
        wishlist = FakeWishlistRepository()
        cart = FakeCartRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(productId: String = "p1") =
        ProductDetailsViewModel(
            productId,
            GetProductDetailsUseCase(repository),
            ObserveFavoriteIdsUseCase(wishlist),
            ToggleFavoriteUseCase(wishlist),
            AddToCartUseCase(cart),
        )

    private fun ProductDetailsUiState.loaded(): ProductDetailsUi = (product as UiState.Success).data

    @Test
    fun `starts Loading before the product is advanced`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        assertEquals(UiState.Loading, viewModel.uiState.value.product)
    }

    @Test
    fun `requests the product with the given id`() = runTest(testDispatcher) {
        viewModel("product-7")

        advanceUntilIdle()

        assertEquals(listOf("product-7"), repository.productCalls)
    }

    @Test
    fun `success shows the product`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Success(product("p1", price = 3500, images = listOf("u1", "u2")))
        val viewModel = viewModel()

        advanceUntilIdle()

        val ui = viewModel.uiState.value.loaded()
        assertEquals("Product p1", ui.title)
        assertEquals(listOf("u1", "u2"), ui.images)
        assertEquals(25, ui.soldCount)
    }

    @Test
    fun `a discounted product uses the discounted price as unit price`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Success(product("p1", price = 500, priceAfterDiscount = 350))
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(350, viewModel.uiState.value.loaded().unitPrice)
    }

    @Test
    fun `failure keeps the specific error`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Failure(AppError.Server(404))
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(UiState.Error(AppError.Server(404)), viewModel.uiState.value.product)
    }

    @Test
    fun `retry after a failure loads the product`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()

        repository.productResult = AppResult.Success(product("p1"))
        viewModel.onRetry()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.product is UiState.Success)
    }

    @Test
    fun `total price is zero while there is no product`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        assertEquals(0, viewModel.uiState.value.totalPrice)
    }

    @Test
    fun `total price starts at the unit price`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Success(product("p1", price = 3500))
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(3500, viewModel.uiState.value.totalPrice)
    }

    @Test
    fun `incrementing the quantity multiplies the total`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Success(product("p1", price = 500, priceAfterDiscount = 350))
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onIncrementQuantity()
        viewModel.onIncrementQuantity()

        assertEquals(3, viewModel.uiState.value.quantity)
        assertEquals(1050, viewModel.uiState.value.totalPrice)
    }

    @Test
    fun `quantity never drops below one`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onDecrementQuantity()

        assertEquals(1, viewModel.uiState.value.quantity)
    }

    @Test
    fun `decrementing lowers the quantity and the total`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Success(product("p1", price = 100))
        val viewModel = viewModel()
        advanceUntilIdle()
        viewModel.onIncrementQuantity()
        viewModel.onIncrementQuantity()

        viewModel.onDecrementQuantity()

        assertEquals(200, viewModel.uiState.value.totalPrice)
    }

    @Test
    fun `quantity survives a retry`() = runTest(testDispatcher) {
        repository.productResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()
        viewModel.onIncrementQuantity()

        repository.productResult = AppResult.Success(product("p1"))
        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.quantity)
    }

    @Test
    fun `toggling favorite flips the flag for this product`() = runTest(testDispatcher) {
        val viewModel = viewModel("p1")
        advanceUntilIdle()

        viewModel.onToggleFavorite()
        advanceUntilIdle()
        assertEquals(listOf("p1"), wishlist.toggleCalls)
        assertTrue(viewModel.uiState.value.isFavorite)
        viewModel.onToggleFavorite()
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isFavorite)
    }

    @Test
    fun `starts favorite when the product is already in the wishlist`() = runTest(testDispatcher) {
        wishlist.setIds(setOf("p1", "other"))
        val viewModel = viewModel("p1")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isFavorite)
    }

    @Test
    fun `a favorite of another product does not fill this heart`() = runTest(testDispatcher) {
        wishlist.setIds(setOf("other"))
        val viewModel = viewModel("p1")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFavorite)
    }

    @Test
    fun `a failed toggle leaves the heart empty`() = runTest(testDispatcher) {
        wishlist.toggleError = AppError.Network
        val viewModel = viewModel("p1")
        advanceUntilIdle()

        viewModel.onToggleFavorite()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isFavorite)
    }

    @Test
    fun `add to cart sends the selected quantity for this product`() = runTest(testDispatcher) {
        val viewModel = viewModel("p9")
        advanceUntilIdle()
        viewModel.onIncrementQuantity()
        viewModel.onIncrementQuantity()

        viewModel.onAddToCart()
        advanceUntilIdle()

        assertEquals(listOf("p9" to 3), cart.addCalls)
    }
}
