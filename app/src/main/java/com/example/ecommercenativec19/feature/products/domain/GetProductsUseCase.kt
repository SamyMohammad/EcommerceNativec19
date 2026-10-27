package com.example.ecommercenativec19.feature.products.domain

import com.example.ecommercenativec19.core.common.AppResult
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(
        categoryId: String? = null,
        subCategoryId: String? = null,
        page: Int = FIRST_PAGE,
        sort: ProductSort? = null,
    ): AppResult<ProductPage> = repository.getProducts(categoryId, subCategoryId, page, sort)

    companion object {
        const val FIRST_PAGE = 1
    }
}
