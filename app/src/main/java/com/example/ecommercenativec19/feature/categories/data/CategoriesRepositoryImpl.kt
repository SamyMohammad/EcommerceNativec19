package com.example.ecommercenativec19.feature.categories.data

import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.safeApiCall
import com.example.ecommercenativec19.feature.categories.domain.CategoriesRepository
import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.SubCategory

class CategoriesRepositoryImpl(private val api: CategoriesApi) : CategoriesRepository {
    override suspend fun getCategories(): AppResult<List<Category>> = safeApiCall {
        api.getCategories().data.map { it.toDomain() }
    }

    override suspend fun getSubCategories(categoryId: String): AppResult<List<SubCategory>> = safeApiCall {
        api.getSubCategories(categoryId).data.map { it.toDomain() }
    }
}
