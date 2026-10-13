package com.example.ecommercenativec19.feature.productlist

import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.cart.FakeCartRepository
import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi
import com.example.ecommercenativec19.feature.products.FakeProductsRepository
import com.example.ecommercenativec19.feature.products.domain.GetProductsUseCase
import com.example.ecommercenativec19.feature.products.domain.ProductPage
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
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

    private fun viewModel(subCategoryId: String = "sub-1") =
        ProductListViewModel(
            subCategoryId,
            GetProductsUseCase(repository),
            ObserveFavoriteIdsUseCase(wishlist),
            ToggleFavoriteUseCase(wishlist),
            AddToCartUseCase(cart),
        )

    private fun ProductListUiState.cards(): List<ProductCardUi> = (products as LoadState.Content).data

    private fun page(vararg ids: String, nextPage: Int? = null) =
        AppResult.Success(ProductPage(ids.map { product(it) }, nextPage))

    @Test
    fun `starts Loading before the first page is advanced`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        assertEquals(LoadState.Loading, viewModel.uiState.value.products)
    }

    @Test
    fun `first page success shows the products as cards`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", "b")
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(listOf("a", "b"), viewModel.uiState.value.cards().map { it.id })
    }

    @Test
    fun `loads the products of the given sub-category starting at page one`() = runTest(testDispatcher) {
        viewModel("sub-9")

        advanceUntilIdle()

        val call = repository.listCalls.single()
        assertEquals("sub-9", call.subCategoryId)
        assertEquals(null, call.categoryId)
        assertEquals(1, call.page)
    }

    @Test
    fun `a discounted product shows the discounted price and the original as old price`() = runTest(testDispatcher) {
        repository.pageResults[1] = AppResult.Success(
            ProductPage(listOf(product("a", price = 500, priceAfterDiscount = 350)), nextPage = null),
        )
        val viewModel = viewModel()

        advanceUntilIdle()

        val card = viewModel.uiState.value.cards().single()
        assertEquals(350, card.price)
        assertEquals(500, card.oldPrice)
    }

    @Test
    fun `a product without discount has no old price`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a")
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(null, viewModel.uiState.value.cards().single().oldPrice)
    }

    @Test
    fun `an empty first page is Empty`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(LoadState.Empty, viewModel.uiState.value.products)
    }

    @Test
    fun `a failed first page keeps the specific error`() = runTest(testDispatcher) {
        repository.pageResults[1] = AppResult.Failure(AppError.Server(503))
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(LoadState.Error(AppError.Server(503)), viewModel.uiState.value.products)
    }

    @Test
    fun `retry after a failure reloads into Content`() = runTest(testDispatcher) {
        repository.pageResults[1] = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()

        repository.pageResults[1] = page("a")
        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(listOf("a"), viewModel.uiState.value.cards().map { it.id })
    }

    @Test
    fun `retry does nothing while content is shown`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a")
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(1, repository.listCalls.size)
    }

    @Test
    fun `load more appends the next page`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", "b", nextPage = 2)
        repository.pageResults[2] = page("c")
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(listOf("a", "b", "c"), viewModel.uiState.value.cards().map { it.id })
        assertEquals(2, repository.listCalls.last().page)
        assertEquals(LoadMoreState.Idle, viewModel.uiState.value.loadMore)
    }

    @Test
    fun `load more shows the Loading footer while the page is in flight`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", nextPage = 2)
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLoadMore()

        assertEquals(LoadMoreState.Loading, viewModel.uiState.value.loadMore)
    }

    @Test
    fun `load more ignores a repeated call while a page is loading`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", nextPage = 2)
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLoadMore()
        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(2, repository.listCalls.size)
    }

    @Test
    fun `load more does nothing on the last page`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", nextPage = null)
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(1, repository.listCalls.size)
    }

    @Test
    fun `a failed load more keeps the list and shows the Error footer`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", nextPage = 2)
        repository.pageResults[2] = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(listOf("a"), viewModel.uiState.value.cards().map { it.id })
        assertEquals(LoadMoreState.Error, viewModel.uiState.value.loadMore)
    }

    @Test
    fun `load more can be retried after a failure`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", nextPage = 2)
        repository.pageResults[2] = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()
        viewModel.onLoadMore()
        advanceUntilIdle()

        repository.pageResults[2] = page("b")
        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(listOf("a", "b"), viewModel.uiState.value.cards().map { it.id })
    }

    @Test
    fun `an item repeated on the next page is not duplicated`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", "b", nextPage = 2)
        repository.pageResults[2] = page("b", "c")
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(listOf("a", "b", "c"), viewModel.uiState.value.cards().map { it.id })
    }

    @Test
    fun `toggling favorite marks only that product`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", "b")
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onToggleFavorite("b")
        advanceUntilIdle()

        assertEquals(setOf("b"), viewModel.uiState.value.favoriteIds)
    }

    @Test
    fun `toggling favorite twice clears it`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onToggleFavorite("b")
        advanceUntilIdle()
        viewModel.onToggleFavorite("b")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.favoriteIds.isEmpty())
    }

    @Test
    fun `hearts follow favorites changed on another screen`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        wishlist.setIds(setOf("x"))
        advanceUntilIdle()

        assertEquals(setOf("x"), viewModel.uiState.value.favoriteIds)
    }

    @Test
    fun `favorites survive loading more`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", nextPage = 2)
        repository.pageResults[2] = page("b")
        val viewModel = viewModel()
        advanceUntilIdle()
        viewModel.onToggleFavorite("a")
        advanceUntilIdle()

        viewModel.onLoadMore()
        advanceUntilIdle()

        assertEquals(setOf("a"), viewModel.uiState.value.favoriteIds)
    }

    @Test
    fun `query change is stored without filtering products`() = runTest(testDispatcher) {
        repository.pageResults[1] = page("a", "b")
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onQueryChange("nike")

        assertEquals("nike", viewModel.uiState.value.query)
        assertEquals(2, viewModel.uiState.value.cards().size)
    }

    @Test
    fun `the add button puts one unit of that product in the cart`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onAddToCart("p2")
        advanceUntilIdle()

        assertEquals(listOf("p2" to 1), cart.addCalls)
    }
}
