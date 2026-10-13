package com.example.ecommercenativec19.feature.categories

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
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

        override suspend fun getCategories(): AppResult<List<Category>> = categoriesResult

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

    @Test
    fun `rail and content start Loading before the init load is advanced`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        assertEquals(LoadStatus.Loading, viewModel.uiState.value.railStatus)
        assertEquals(LoadStatus.Loading, viewModel.uiState.value.contentStatus)
    }

    @Test
    fun `categories success selects the first category and loads its sub-categories`() = runTest(testDispatcher) {
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(LoadStatus.Content, state.railStatus)
        assertEquals(listOf("c1"), state.railCategories.filter { it.isSelected }.map { it.id })
        assertEquals("Men", state.selectedTitle)
        assertEquals(LoadStatus.Content, state.contentStatus)
        assertEquals(listOf("s-c1"), state.subCategories.map { it.id })
    }

    @Test
    fun `categories failure sets both rail and content to Error`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(LoadStatus.Error, state.railStatus)
        assertEquals(LoadStatus.Error, state.contentStatus)
    }

    @Test
    fun `onRetry after a rail failure reloads categories into Content`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Failure(AppError.Network)
        val viewModel = viewModel()
        advanceUntilIdle()

        fakeRepository.categoriesResult = AppResult.Success(listOf(Category("c1", "Men", "img1")))
        viewModel.onRetry()
        advanceUntilIdle()

        assertEquals(LoadStatus.Content, viewModel.uiState.value.railStatus)
    }

    @Test
    fun `empty categories list sets rail and content to Empty`() = runTest(testDispatcher) {
        fakeRepository.categoriesResult = AppResult.Success(emptyList())
        val viewModel = viewModel()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(LoadStatus.Empty, state.railStatus)
        assertEquals(LoadStatus.Empty, state.contentStatus)
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
            assertEquals(listOf("c2"), state.railCategories.filter { it.isSelected }.map { it.id })
            assertEquals("Women", state.selectedTitle)
            assertEquals(listOf("c2"), fakeRepository.requestedSubCategoryIds)
            assertEquals(listOf("s-c2"), state.subCategories.map { it.id })
        }

    @Test
    fun `empty sub-categories list sets content status to Empty`() = runTest(testDispatcher) {
        fakeRepository.subCategoriesResults["c1"] = AppResult.Success(emptyList())
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(LoadStatus.Empty, viewModel.uiState.value.contentStatus)
    }

    @Test
    fun `sub-categories failure sets content Error, and onRetry reloads the selected category`() =
        runTest(testDispatcher) {
            fakeRepository.subCategoriesResults["c1"] = AppResult.Failure(AppError.Network)
            val viewModel = viewModel()
            advanceUntilIdle()
            assertEquals(LoadStatus.Error, viewModel.uiState.value.contentStatus)

            fakeRepository.subCategoriesResults["c1"] = AppResult.Success(listOf(SubCategory("s-c1", "Sub of c1")))
            viewModel.onRetry()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(LoadStatus.Content, state.contentStatus)
            assertEquals(listOf("s-c1"), state.subCategories.map { it.id })
        }

    @Test
    fun `selecting a second category while the first's sub-categories call is still suspended shows the second's data`() =
        runTest(testDispatcher) {
            // Holds the init load's sub-category call for "c1" suspended indefinitely.
            fakeRepository.subCategoriesGates["c1"] = CompletableDeferred()
            val viewModel = viewModel()
            advanceUntilIdle()
            assertEquals(LoadStatus.Content, viewModel.uiState.value.railStatus)
            assertEquals(LoadStatus.Loading, viewModel.uiState.value.contentStatus)

            viewModel.onCategorySelected("c2")
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("c2", state.railCategories.first { it.isSelected }.id)
            assertEquals(LoadStatus.Content, state.contentStatus)
            assertEquals(listOf("s-c2"), state.subCategories.map { it.id })
        }

    @Test
    fun `selecting an unknown category id is ignored`() = runTest(testDispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()
        fakeRepository.requestedSubCategoryIds.clear()

        viewModel.onCategorySelected("does-not-exist")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(listOf("c1"), state.railCategories.filter { it.isSelected }.map { it.id })
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
