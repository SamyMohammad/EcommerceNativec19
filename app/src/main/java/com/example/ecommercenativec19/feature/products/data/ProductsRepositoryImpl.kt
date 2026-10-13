package com.example.ecommercenativec19.feature.products.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.safeApiCall
import com.example.ecommercenativec19.feature.products.domain.Product
import com.example.ecommercenativec19.feature.products.domain.ProductPage
import com.example.ecommercenativec19.feature.products.domain.ProductsRepository

class ProductsRepositoryImpl(private val api: ProductsApi) : ProductsRepository {
    override suspend fun getProducts(
        categoryId: String?,
        subCategoryId: String?,
        page: Int,
    ): AppResult<ProductPage> = safeApiCall {
        val response = api.getProducts(categoryId, subCategoryId, page, PAGE_SIZE)
        ProductPage(
            items = response.data.mapNotNull { it.toDomain() },
            nextPage = response.metadata?.nextPage,
        )
    }

    override suspend fun getProduct(id: String): AppResult<Product> =
        when (val result = safeApiCall { api.getProduct(id).data }) {
            is AppResult.Failure -> result
            // A 200 whose product has no usable id/title/price is a contract violation, not an empty result.
            is AppResult.Success -> result.data.toDomain()
                ?.let { AppResult.Success(it) }
                ?: AppResult.Failure(AppError.Parsing)
        }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
