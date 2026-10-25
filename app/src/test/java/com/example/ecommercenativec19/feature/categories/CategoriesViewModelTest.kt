package com.example.ecommercenativec19.feature.categories

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.categories.domain.CategoriesRepository
import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.GetCategoriesUseCase
import com.example.ecommercenativec19.feature.categories.domain.GetSubCategoriesUseCase
import com.example.ecommercenativec19.feature.categories.domain.SubCategory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * [CategoriesViewModel] state transition tests. A [FakeCategoriesRepository] stands in for the data layer so these
 * run without a dispatcher-driven coroutine race: [StandardTestDispatcher] queues work until [advanceUntilIdle].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    /** Hand-written fake: [subCategoriesGates] lets a test hold a specific category's sub-category call suspended. */
    private class FakeCategoriesRepository : CategoriesRepository {
        var categoriesResult: AppResult<List<Category>> =
            AppResult.Success(listOf(Category("c1", "Men", "img1"), Category("c2", "Women", "img2")))
        val subCategoriesResults = mutableMapOf<String, AppResult<List<SubCategory>>>()
        val subCategoriesGates = mutableMapOf<String, CompletableDeferred<Unit>>()
        val requestedSubCategoryIds = mutableListOf<String>()

        /** Holds the next categories call suspended until the test completes the gate. */
        var categoriesGate: CompletableDeferred<Unit>? = null
        var categoriesCalls = 0

        override suspend fun getCategories(): AppResult<List<Category>> {
            categoriesCalls++
            categoriesGate?.await()
            return categoriesResult
        }

        override suspend fun getSubCategories(categoryId: String): AppResult<List<SubCategory>> {
            requestedSubCategoryIds.add(categoryId)
            subCategoriesGates[categoryId]?.await()
            return subCategoriesResults[categoryId]
                ?: AppResult.Success(listOf(SubCategory("s-$categoryId", "Sub of $categoryId")))
        }
    }

    private lateinit var fakeRepository: FakeCategoriesRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeCategoriesRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() =
        CategoriesViewModel(GetCategoriesUseCase(fakeRepository), GetSubCategoriesUseCase(fakeRepository))

    private fun CategoriesUiState.rail(): List<Category> = (categories as UiState.Success).data

    private fun CategoriesUiState.subs(): List<SubCategory> = (subCategories as UiState.Success).data

    @Test
    fun `rail and content start Loading before the init load is advanced`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        assertEquals(UiState.Loading, viewModel.uiState.value.categories)
        assertEquals(UiState.Loading, viewModel.uiState.value.subCategories)
    }

    @Test
    fun `categories success selects the first category and loads its sub-categories`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("c1", state.selectedCategoryId)
        assertEquals("Men", state.selectedTitle)
        assertEquals(listOf("s-c1"), state.subs().map { it.id })
    }

    @Test
    fun `categories failure sets both rail and content to the same Error`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(UiState.Error(AppError.Network), state.categories)
        assertEquals(UiState.Error(AppError.Network), state.subCategories)
    }

    @Test
    fun `categories failure preserves the specific error`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Failure(AppError.Server(503))
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(UiState.Error(AppError.Server(503)), viewModel.uiState.value.categories)
    }

    @Test
    fun `sub-categories failure preserves the specific error`() = runTest(testDispatcher) {
        fakeRepository.subCategoriesResults["c1"] = AppResult.Failure(AppError.Parsing)
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(UiState.Error(AppError.Parsing), viewModel.uiState.value.subCategories)
    }

    @Test
    fun `onRetry after a rail failure reloads categories into Content`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()

        fakeRepository.categoriesResult = AppResult.Success(listOf(Category("c1", "Men", "img1")))
        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(listOf("c1"), viewModel.uiState.value.rail().map { it.id })
    }

    @Test
    fun `onRetry while a categories retry is in flight does not start another load`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()
        fakeRepository.categoriesGate = CompletableDeferred()

        viewModel.onRetry()
        viewModel.onRetry()
        advanceUntilIdle()

        // One call for the init load, one for the first retry; the second retry saw Loading and did nothing.
        assertEquals(2, fakeRepository.categoriesCalls)
        assertEquals(UiState.Loading, viewModel.uiState.value.categories)
    }

    @Test
    fun `empty categories list sets rail and content to Empty`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Success(emptyList())
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(UiState.Empty, state.categories)
        assertEquals(UiState.Empty, state.subCategories)
    }

    @Test
    fun `selecting a category moves the selection and loads that category's sub-categories`() =
        runTest(testDispatcher) {
            val viewModel = viewModel()
            advanceUntilIdle()
            fakeRepository.requestedSubCategoryIds.clear()

            viewModel.onCategorySelected("c2")
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("c2", state.selectedCategoryId)
            assertEquals("Women", state.selectedTitle)
            assertEquals(listOf("c2"), fakeRepository.requestedSubCategoryIds)
            assertEquals(listOf("s-c2"), state.subs().map { it.id })
        }

    @Test
    fun `empty sub-categories list sets content to Empty`() = runTest(testDispatcher) {
        fakeRepository.subCategoriesResults["c1"] = AppResult.Success(emptyList())
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(UiState.Empty, viewModel.uiState.value.subCategories)
    }

    @Test
    fun `sub-categories failure sets content Error, and onRetry reloads the selected category`() =
        runTest(testDispatcher) {
            fakeRepository.subCategoriesResults["c1"] = AppResult.Failure(AppError.Network)
            val viewModel = viewModel()
            advanceUntilIdle()
            assertEquals(UiState.Error(AppError.Network), viewModel.uiState.value.subCategories)

            fakeRepository.subCategoriesResults["c1"] = AppResult.Success(listOf(SubCategory("s-c1", "Sub of c1")))
            viewModel.onRetry()
            advanceUntilIdle()

            assertEquals(listOf("s-c1"), viewModel.uiState.value.subs().map { it.id })
        }

    @Test
    fun `selecting a second category while the first's sub-categories call is still suspended shows the second's data`() =
        runTest(testDispatcher) {
            // Holds the init load's sub-category call for "c1" suspended indefinitely.
            fakeRepository.subCategoriesGates["c1"] = CompletableDeferred()
            val viewModel = viewModel()
            advanceUntilIdle()
            assertEquals(UiState.Loading, viewModel.uiState.value.subCategories)

            viewModel.onCategorySelected("c2")
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("c2", state.selectedCategoryId)
            assertEquals(listOf("s-c2"), state.subs().map { it.id })
        }

    @Test
    fun `selecting an unknown category id is ignored`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()
        fakeRepository.requestedSubCategoryIds.clear()

        viewModel.onCategorySelected("does-not-exist")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("c1", state.selectedCategoryId)
        assertEquals("Men", state.selectedTitle)
        assertEquals(emptyList<String>(), fakeRepository.requestedSubCategoryIds)
    }

    @Test
    fun `search query change updates state`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onSearchQueryChange("shirt")

        assertEquals("shirt", viewModel.uiState.value.searchQuery)
    }
}
