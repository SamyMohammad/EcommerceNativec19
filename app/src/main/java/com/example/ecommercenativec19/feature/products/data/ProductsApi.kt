package com.example.ecommercenativec19.feature.products.data

import com.example.ecommercenativec19.core.network.ItemResponse
import com.example.ecommercenativec19.core.network.ListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    /** Both filters are optional; Retrofit omits a null query value. The server caps [limit] at 50. */
    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("category") categoryId: String?,
        @Query("subcategory") subCategoryId: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ListResponse<ProductDto>

    @GET("api/v1/products/{id}")
    suspend fun getProduct(@Path("id") id: String): ItemResponse<ProductDto>
}
