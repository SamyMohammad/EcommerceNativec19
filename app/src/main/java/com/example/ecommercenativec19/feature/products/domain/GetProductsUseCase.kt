package com.example.ecommercenativec19.feature.products.domain

import com.example.ecommercenativec19.core.common.AppResult

class GetProductsUseCase(private val repository: ProductsRepository) {
    suspend operator fun invoke(
        categoryId: String? = null,
        subCategoryId: String? = null,
        page: Int = FIRST_PAGE,
    ): AppResult<ProductPage> = repository.getProducts(categoryId, subCategoryId, page)

    companion object {
        const val FIRST_PAGE = 1
    }
}
