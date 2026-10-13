package com.example.ecommercenativec19.feature.categories.data

import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.ListResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/** Verifies the repository maps DTOs to domain models and lets [com.example.ecommercenativec19.core.network.safeApiCall] handle failures. */
class CategoriesRepositoryImplTest {

    private class FakeCategoriesApi : CategoriesApi {
        var categoriesResponse: ListResponse<CategoryDto> = ListResponse(emptyList())
        var subCategoriesResponse: ListResponse<SubCategoryDto> = ListResponse(emptyList())
        var shouldThrow: Boolean = false
        var lastRequestedCategoryId: String? = null

        override suspend fun getCategories(): ListResponse<CategoryDto> {
            if (shouldThrow) throw IOException("no connection")
            return categoriesResponse
        }

        override suspend fun getSubCategories(categoryId: String): ListResponse<SubCategoryDto> {
            lastRequestedCategoryId = categoryId
            if (shouldThrow) throw IOException("no connection")
            return subCategoriesResponse
        }
    }

    private lateinit var fakeApi: FakeCategoriesApi
    private lateinit var repository: CategoriesRepositoryImpl

    @Test
    fun `getCategories maps dto list to domain categories`() = runTest {
        fakeApi = FakeCategoriesApi().apply {
            categoriesResponse = ListResponse(
                listOf(CategoryDto(id = "c1", name = "Men's Fashion", image = "img1")),
            )
        }
        repository = CategoriesRepositoryImpl(fakeApi)

        val result = repository.getCategories()

        assertEquals(
            AppResult.Success(
                listOf(
                    com.example.ecommercenativec19.feature.categories.domain.Category(
                        id = "c1",
                        name = "Men's Fashion",
                        imageUrl = "img1",
                    ),
                ),
            ),
            result,
        )
    }

    @Test
    fun `getCategories returns Failure when the api call throws`() = runTest {
        fakeApi = FakeCategoriesApi().apply { shouldThrow = true }
        repository = CategoriesRepositoryImpl(fakeApi)

        val result = repository.getCategories()

        assertTrue(result is AppResult.Failure)
    }

    @Test
    fun `getSubCategories maps dto list to domain sub-categories`() = runTest {
        fakeApi = FakeCategoriesApi().apply {
            subCategoriesResponse = ListResponse(
                listOf(SubCategoryDto(id = "s1", name = "Men's Clothing")),
            )
        }
        repository = CategoriesRepositoryImpl(fakeApi)

        val result = repository.getSubCategories("c1")

        assertEquals(
            AppResult.Success(
                listOf(com.example.ecommercenativec19.feature.categories.domain.SubCategory(id = "s1", name = "Men's Clothing")),
            ),
            result,
        )
    }

    @Test
    fun `getSubCategories passes the requested category id through to the api`() = runTest {
        fakeApi = FakeCategoriesApi()
        repository = CategoriesRepositoryImpl(fakeApi)

        repository.getSubCategories("category-42")

        assertEquals("category-42", fakeApi.lastRequestedCategoryId)
    }

    @Test
    fun `getSubCategories returns Failure when the api call throws`() = runTest {
        fakeApi = FakeCategoriesApi().apply { shouldThrow = true }
        repository = CategoriesRepositoryImpl(fakeApi)

        val result = repository.getSubCategories("c1")

        assertTrue(result is AppResult.Failure)
    }
}
