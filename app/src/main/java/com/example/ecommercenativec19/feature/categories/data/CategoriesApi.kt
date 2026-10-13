package com.example.ecommercenativec19.feature.categories.data

import com.example.ecommercenativec19.core.network.ListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoriesApi {
    @GET("api/v1/categories")
    suspend fun getCategories(): ListResponse<CategoryDto>

    // Not /categories/{id}/subcategories: the server ignores the id there and returns every sub-category.
    @GET("api/v1/subcategories")
    suspend fun getSubCategories(@Query("category") categoryId: String): ListResponse<SubCategoryDto>
}
