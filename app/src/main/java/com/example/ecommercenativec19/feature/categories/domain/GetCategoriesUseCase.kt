package com.example.ecommercenativec19.feature.categories.domain

import com.example.ecommercenativec19.core.common.AppResult
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: CategoriesRepository) {
    suspend operator fun invoke(): AppResult<List<Category>> = repository.getCategories()
}
