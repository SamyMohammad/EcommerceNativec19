package com.example.ecommercenativec19.feature.products.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.ItemResponse
import com.example.ecommercenativec19.core.network.ListResponse
import com.example.ecommercenativec19.core.network.PageMetadataDto
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ProductsRepositoryImplTest {

    private class FakeProductsApi : ProductsApi {
        var listResponse: ListResponse<ProductDto> = ListResponse(emptyList())
        var itemResponse: ItemResponse<ProductDto> = ItemResponse(validDto("p1"))
        var failure: Exception? = null

        var lastCategoryId: String? = "unset"
        var lastSubCategoryId: String? = "unset"
        var lastPage: Int? = null
        var lastLimit: Int? = null
        var lastProductId: String? = null

        override suspend fun getProducts(
            categoryId: String?,
            subCategoryId: String?,
            page: Int,
            limit: Int,
        ): ListResponse<ProductDto> {
            lastCategoryId = categoryId
            lastSubCategoryId = subCategoryId
            lastPage = page
            lastLimit = limit
            failure?.let { throw it }
            return listResponse
        }

        override suspend fun getProduct(id: String): ItemResponse<ProductDto> {
            lastProductId = id
            failure?.let { throw it }
            return itemResponse
        }
    }

    private val api = FakeProductsApi()
    private val repository = ProductsRepositoryImpl(api)

    @Test
    fun `getProducts maps the dtos to domain products`() = runTest {
        api.listResponse = ListResponse(listOf(validDto("a"), validDto("b")))

        val page = (repository.getProducts(null, "sub", 1) as AppResult.Success).data

        assertEquals(listOf("a", "b"), page.items.map { it.id })
    }

    @Test
    fun `getProducts drops products the app cannot show`() = runTest {
        api.listResponse = ListResponse(listOf(validDto("a"), validDto(" "), ProductDto(id = "c", title = "No price")))

        val page = (repository.getProducts(null, "sub", 1) as AppResult.Success).data

        assertEquals(listOf("a"), page.items.map { it.id })
    }

    @Test
    fun `getProducts reports the next page from the metadata`() = runTest {
        api.listResponse = ListResponse(listOf(validDto("a")), PageMetadataDto(nextPage = 2))

        val page = (repository.getProducts(null, "sub", 1) as AppResult.Success).data

        assertEquals(2, page.nextPage)
    }

    @Test
    fun `getProducts has no next page without metadata`() = runTest {
        api.listResponse = ListResponse(listOf(validDto("a")))

        val page = (repository.getProducts(null, "sub", 1) as AppResult.Success).data

        assertEquals(null, page.nextPage)
    }

    @Test
    fun `getProducts passes the filters and page to the api`() = runTest {
        repository.getProducts(categoryId = "cat-1", subCategoryId = "sub-2", page = 3)

        assertEquals("cat-1", api.lastCategoryId)
        assertEquals("sub-2", api.lastSubCategoryId)
        assertEquals(3, api.lastPage)
    }

    @Test
    fun `getProducts leaves an unused filter null`() = runTest {
        repository.getProducts(categoryId = null, subCategoryId = "sub-2", page = 1)

        assertEquals(null, api.lastCategoryId)
    }

    @Test
    fun `getProducts requests a page size within the server cap of 50`() = runTest {
        repository.getProducts(null, "sub", 1)

        assertEquals(20, api.lastLimit)
    }

    @Test
    fun `getProducts maps an IOException to Network`() = runTest {
        api.failure = IOException("offline")

        assertEquals(AppResult.Failure(AppError.Network), repository.getProducts(null, "sub", 1))
    }

    @Test
    fun `getProducts maps an HTTP error to Server with its code`() = runTest {
        api.failure = HttpException(Response.error<Unit>(500, "{}".toResponseBody("application/json".toMediaType())))

        assertEquals(AppResult.Failure(AppError.Server(500)), repository.getProducts(null, "sub", 1))
    }

    @Test
    fun `getProduct maps the dto to a domain product`() = runTest {
        api.itemResponse = ItemResponse(validDto("p9"))

        val product = (repository.getProduct("p9") as AppResult.Success).data

        assertEquals("p9", product.id)
    }

    @Test
    fun `getProduct passes the id to the api`() = runTest {
        repository.getProduct("abc")

        assertEquals("abc", api.lastProductId)
    }

    @Test
    fun `getProduct reports Parsing when the product is unusable`() = runTest {
        api.itemResponse = ItemResponse(ProductDto(id = "p1", title = "No price"))

        assertEquals(AppResult.Failure(AppError.Parsing), repository.getProduct("p1"))
    }

    @Test
    fun `getProduct maps a 404 to Server 404`() = runTest {
        api.failure = HttpException(Response.error<Unit>(404, "{}".toResponseBody("application/json".toMediaType())))

        assertEquals(AppResult.Failure(AppError.Server(404)), repository.getProduct("missing"))
    }

    @Test
    fun `getProduct maps an IOException to Network`() = runTest {
        api.failure = IOException("offline")

        assertEquals(AppResult.Failure(AppError.Network), repository.getProduct("p1"))
    }
}

private fun validDto(id: String) = ProductDto(id = id, title = "Product $id", price = 100)
