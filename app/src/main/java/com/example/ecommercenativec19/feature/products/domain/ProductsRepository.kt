package com.example.ecommercenativec19.feature.products.domain

import com.example.ecommercenativec19.core.common.AppResult

interface ProductsRepository {
    /** [categoryId] and [subCategoryId] are optional filters, [sort] an optional ordering; pages start at 1. */
    suspend fun getProducts(
        categoryId: String?,
        subCategoryId: String?,
        page: Int,
        sort: ProductSort? = null,
    ): AppResult<ProductPage>

    suspend fun getProduct(id: String): AppResult<Product>
}
