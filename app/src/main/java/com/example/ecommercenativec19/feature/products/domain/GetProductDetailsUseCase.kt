package com.example.ecommercenativec19.feature.products.domain

import com.example.ecommercenativec19.core.common.AppResult
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(private val repository: ProductsRepository) {
    suspend operator fun invoke(id: String): AppResult<Product> = repository.getProduct(id)
}
