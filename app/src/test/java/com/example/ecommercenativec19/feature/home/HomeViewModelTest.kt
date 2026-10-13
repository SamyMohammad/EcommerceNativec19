package com.example.ecommercenativec19.feature.home

import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.cart.FakeCartRepository
import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.categories.domain.CategoriesRepository
import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.GetCategoriesUseCase
import com.example.ecommercenativec19.feature.categories.domain.SubCategory
import com.example.ecommercenativec19.feature.products.FakeProductsRepository
import com.example.ecommercenativec19.feature.products.domain.GetProductsUseCase
import com.example.ecommercenativec19.feature.products.domain.ProductPage
import com.example.ecommercenativec19.feature.products.domain.ProductSort
import com.example.ecommercenativec19.feature.products.product
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi
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
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private class FakeCategoriesRepository : CategoriesRepository {
        var result: AppResult<List<Category>> =
            AppResult.Success(listOf(Category("c1", "Men", "img1"), Category("c2", "Women", null)))
        var calls = 0

        override suspend fun getCategories(): AppResult<List<Category>> {
            calls++
            return result
        }

        override suspend fun getSubCategories(categoryId: String): AppResult<List<SubCategory>> =
            error("Home never loads sub-categories")
    }

    private lateinit var categories: FakeCategoriesRepository
    private lateinit var products: FakeProductsRepository
    private lateinit var wishlist: FakeWishlistRepository
    private lateinit var cart: FakeCartRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        categories = FakeCategoriesRepository()
        products = FakeProductsRepository()
        wishlist = FakeWishlistRepository()
        cart = FakeCartRepository()
        ProductSort.entries.forEach { products.sortResults[it] = page("${it.name}-1", "${it.name}-2") }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = HomeViewModel(
        GetCategoriesUseCase(categories),
        GetProductsUseCase(products),
        ObserveFavoriteIdsUseCase(wishlist),
        ToggleFavoriteUseCase(wishlist),
        AddToCartUseCase(cart),
    )

    private fun page(vararg ids: String) = AppResult.Success(ProductPage(ids.map { product(it) }, nextPage = null))

    private fun HomeUiState.cards(section: HomeSection): List<ProductCardUi> =
        (sections.getValue(section) as LoadState.Content).data

    @Test
    fun `every region starts Loading before the loads are advanced`() = runTest(testDispatcher) {
        val state = viewModel().uiState.value

        assertEquals("", state.searchQuery)
        assertEquals(LoadState.Loading, state.categories)
        assertEquals(HomeSection.entries.toSet(), state.sections.keys)
        assertTrue(state.sections.values.all { it == LoadState.Loading })
        assertTrue(state.favoriteIds.isEmpty())
    }

    @Test
    fun `categories success maps name and image url`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        advanceUntilIdle()

        val expected = listOf(HomeCategoryUi("c1", "Men", "img1"), HomeCategoryUi("c2", "Women", null))
        assertEquals(LoadState.Content(expected), viewModel.uiState.value.categories)
    }

    @Test
    fun `an empty category list shows Empty`() = runTest(testDispatcher) {
        categories.result = AppResult.Success(emptyList())
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(LoadState.Empty, viewModel.uiState.value.categories)
    }

    @Test
    fun `each section loads the products of its own ranking`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(listOf("BestSelling-1", "BestSelling-2"), state.cards(HomeSection.BestSellers).map { it.id })
        assertEquals(listOf("Newest-1", "Newest-2"), state.cards(HomeSection.NewArrival).map { it.id })
        assertEquals(listOf("TopRated-1", "TopRated-2"), state.cards(HomeSection.TopRated).map { it.id })
        assertEquals(ProductSort.entries.toSet(), products.listCalls.mapNotNull { it.sort }.toSet())
    }

    @Test
    fun `a section keeps only the first ten products`() = runTest(testDispatcher) {
        products.sortResults[ProductSort.BestSelling] = page(*Array(15) { "p$it" })
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(10, viewModel.uiState.value.cards(HomeSection.BestSellers).size)
    }

    @Test
    fun `a section without products shows Empty`() = runTest(testDispatcher) {
        products.sortResults[ProductSort.Newest] = page()
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(LoadState.Empty, viewModel.uiState.value.sections[HomeSection.NewArrival])
    }

    @Test
    fun `one failing region does not break the others`() = runTest(testDispatcher) {
        categories.result = AppResult.Failure(AppError.Network)
        products.sortResults[ProductSort.TopRated] = AppResult.Failure(AppError.Server(500))
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(LoadState.Error(AppError.Network), state.categories)
        assertEquals(LoadState.Error(AppError.Server(500)), state.sections[HomeSection.TopRated])
        assertEquals(2, state.cards(HomeSection.BestSellers).size)
        assertEquals(2, state.cards(HomeSection.NewArrival).size)
    }

    @Test
    fun `retrying a section reloads only that section`() = runTest(testDispatcher) {
        products.sortResults[ProductSort.TopRated] = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()
        val callsBefore = products.listCalls.size
        products.sortResults[ProductSort.TopRated] = page("again")

        viewModel.onRetrySection(HomeSection.TopRated)
        assertEquals(LoadState.Loading, viewModel.uiState.value.sections[HomeSection.TopRated])
        advanceUntilIdle()

        assertEquals(listOf("again"), viewModel.uiState.value.cards(HomeSection.TopRated).map { it.id })
        assertEquals(callsBefore + 1, products.listCalls.size)
        assertEquals(1, categories.calls)
    }

    @Test
    fun `retrying categories reloads only the categories`() = runTest(testDispatcher) {
        categories.result = AppResult.Failure(AppError.Parsing)
        val viewModel = viewModel()
        advanceUntilIdle()
        val productCalls = products.listCalls.size
        categories.result = AppResult.Success(listOf(Category("c9", "Books", "img")))

        viewModel.onRetryCategories()
        advanceUntilIdle()

        assertEquals(
            LoadState.Content(listOf(HomeCategoryUi("c9", "Books", "img"))),
            viewModel.uiState.value.categories,
        )
        assertEquals(productCalls, products.listCalls.size)
    }

    @Test
    fun `retry is ignored for a region that did not fail`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onRetryCategories()
        viewModel.onRetrySection(HomeSection.BestSellers)
        advanceUntilIdle()

        assertEquals(1, categories.calls)
        assertEquals(HomeSection.entries.size, products.listCalls.size)
    }

    @Test
    fun `search query change updates state`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        viewModel.onSearchQueryChange("watch")

        assertEquals("watch", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `favorite toggle goes through the shared wishlist`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onFavoriteToggle("p1")
        advanceUntilIdle()

        assertEquals(listOf("p1"), wishlist.toggleCalls)
        assertTrue("p1" in viewModel.uiState.value.favoriteIds)
    }

    @Test
    fun `hearts follow the shared favorites, including changes made elsewhere`() = runTest(testDispatcher) {
        wishlist.setIds(setOf("p1"))
        val viewModel = viewModel()
        advanceUntilIdle()
        assertEquals(setOf("p1"), viewModel.uiState.value.favoriteIds)

        wishlist.setIds(emptySet())
        advanceUntilIdle()

        assertFalse("p1" in viewModel.uiState.value.favoriteIds)
    }

    @Test
    fun `a failed toggle leaves the heart as it was`() = runTest(testDispatcher) {
        wishlist.toggleError = AppError.Network
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onFavoriteToggle("p1")
        advanceUntilIdle()

        assertFalse("p1" in viewModel.uiState.value.favoriteIds)
    }

    @Test
    fun `the add button puts one unit of that product in the cart`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onAddToCart("p1")
        advanceUntilIdle()

        assertEquals(listOf("p1" to 1), cart.addCalls)
    }
}
