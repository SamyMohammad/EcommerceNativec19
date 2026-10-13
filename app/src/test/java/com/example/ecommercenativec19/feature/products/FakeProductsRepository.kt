package com.example.ecommercenativec19.feature.products

import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.feature.products.domain.Product
import com.example.ecommercenativec19.feature.products.domain.ProductPage
import com.example.ecommercenativec19.feature.products.domain.ProductSort
import com.example.ecommercenativec19.feature.products.domain.ProductsRepository

/** Hand-written fake shared by the ViewModel tests. Unconfigured pages are empty last pages. */
class FakeProductsRepository : ProductsRepository {
    val pageResults = mutableMapOf<Int, AppResult<ProductPage>>()

    /** Takes precedence over [pageResults] for a call that asks for that sort. */
    val sortResults = mutableMapOf<ProductSort, AppResult<ProductPage>>()
    var productResult: AppResult<Product> = AppResult.Success(product("p1"))

    class ListCall(val categoryId: String?, val subCategoryId: String?, val page: Int, val sort: ProductSort?)

    val listCalls = mutableListOf<ListCall>()
    val productCalls = mutableListOf<String>()

    override suspend fun getProducts(
        categoryId: String?,
        subCategoryId: String?,
        page: Int,
        sort: ProductSort?,
    ): AppResult<ProductPage> {
        listCalls.add(ListCall(categoryId, subCategoryId, page, sort))
        return sort?.let { sortResults[it] } ?: pageResults[page] ?: AppResult.Success(ProductPage(emptyList(), nextPage = null))
    }

    override suspend fun getProduct(id: String): AppResult<Product> {
        productCalls.add(id)
        return productResult
    }
}

fun product(
    id: String,
    price: Int = 100,
    priceAfterDiscount: Int? = null,
    images: List<String> = listOf("https://img.test/$id-1.jpg"),
) = Product(
    id = id,
    title = "Product $id",
    description = "Description of $id",
    price = price,
    priceAfterDiscount = priceAfterDiscount,
    imageUrl = images.firstOrNull(),
    images = images,
    rating = 4.5,
    ratingCount = 10,
    soldCount = 25,
    categoryId = "cat",
    brandName = "Brand",
)
