package com.example.ecommercenativec19.feature.categories.domain

import com.example.ecommercenativec19.core.common.AppResult

interface CategoriesRepository {
    suspend fun getCategories(): AppResult<List<Category>>
    suspend fun getSubCategories(categoryId: String): AppResult<List<SubCategory>>
}
