package com.example.ecommercenativec19.feature.categories.data

import com.example.ecommercenativec19.core.network.ListResponse
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Decodes the real sample payloads documented in docs/api/categories.md to prove the DTOs' @SerialName("_id")
 * mapping and `ignoreUnknownKeys` tolerate the extra fields (slug, createdAt, updatedAt, results, metadata)
 * the live API sends but the app does not model.
 */
class CategoriesJsonParsingTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `decodes categories sample payload ignoring unknown fields`() {
        val payload = """
            {
              "results": 10,
              "metadata": { "currentPage": 1, "numberOfPages": 5, "limit": 2, "nextPage": 2 },
              "data": [
                {
                  "_id": "6439d5b90049ad0b52b90048",
                  "name": "Men's Fashion",
                  "slug": "men's-fashion",
                  "image": "https://ecommerce.routemisr.com/Route-Academy-categories/1681511865180.jpeg",
                  "createdAt": "2023-04-14T22:37:45.627Z",
                  "updatedAt": "2023-04-14T22:37:45.627Z"
                }
              ]
            }
        """.trimIndent()

        val decoded = json.decodeFromString<ListResponse<CategoryDto>>(payload)

        assertEquals(1, decoded.data.size)
        val category = decoded.data.first()
        assertEquals("6439d5b90049ad0b52b90048", category.id)
        assertEquals("Men's Fashion", category.name)
        assertEquals("https://ecommerce.routemisr.com/Route-Academy-categories/1681511865180.jpeg", category.image)
    }

    @Test
    fun `null or missing image does not fail the whole list`() {
        val payload = """
            {
              "data": [
                { "_id": "a", "name": "With null", "image": null },
                { "_id": "b", "name": "Missing" },
                { "_id": "c", "name": "With image", "image": "https://example.com/c.jpg" }
              ]
            }
        """.trimIndent()

        val decoded = json.decodeFromString<ListResponse<CategoryDto>>(payload)

        assertEquals(listOf(null, null, "https://example.com/c.jpg"), decoded.data.map { it.image })
    }

    @Test
    fun `decodes subcategories sample payload ignoring unknown fields`() {
        val payload = """
            {
              "results": 2,
              "metadata": { "currentPage": 1, "numberOfPages": 1, "limit": 40 },
              "data": [
                {
                  "_id": "6407f243b575d3b90bf957ac",
                  "name": "Men's Clothing",
                  "slug": "men's-clothing",
                  "category": "6439d5b90049ad0b52b90048",
                  "createdAt": "2023-03-08T02:26:11.097Z",
                  "updatedAt": "2023-04-14T23:10:29.386Z"
                }
              ]
            }
        """.trimIndent()

        val decoded = json.decodeFromString<ListResponse<SubCategoryDto>>(payload)

        assertEquals(1, decoded.data.size)
        val subCategory = decoded.data.first()
        assertEquals("6407f243b575d3b90bf957ac", subCategory.id)
        assertEquals("Men's Clothing", subCategory.name)
    }
}
