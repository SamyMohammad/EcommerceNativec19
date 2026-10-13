package com.example.ecommercenativec19.feature.products.data

import com.example.ecommercenativec19.core.network.ItemResponse
import com.example.ecommercenativec19.core.network.ListResponse
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Decodes payloads captured from the live API (see docs/api/products.md; reviews and other personal data removed)
 * to prove the DTOs tolerate the many fields the app does not model.
 */
class ProductsJsonParsingTest {

    private val json = Json { ignoreUnknownKeys = true }

    private val listPayload = """
        {
          "results": 56,
          "metadata": { "currentPage": 1, "numberOfPages": 28, "limit": 2, "nextPage": 2 },
          "data": [
            {
              "sold": 2841,
              "images": [
                "https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-1.jpeg",
                "https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-2.jpeg"
              ],
              "subcategory": [
                { "_id": "6407f1bcb575d3b90bf95797", "name": "Women's Clothing", "slug": "women's-clothing", "category": "6439d58a0049ad0b52b9003f" }
              ],
              "ratingsQuantity": 46,
              "_id": "6428ebc6dc1175abc65ca0b9",
              "title": "Woman Shawl",
              "slug": "woman-shawl",
              "description": "Material\tPolyester Blend\nColour Name\tMulticolour",
              "quantity": 220,
              "price": 149,
              "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
              "category": {
                "_id": "6439d58a0049ad0b52b9003f",
                "name": "Women's Fashion",
                "slug": "women's-fashion",
                "image": "https://ecommerce.routemisr.com/Route-Academy-categories/1681511818071.jpeg"
              },
              "brand": {
                "_id": "64089bbe24b25627a253158b",
                "name": "DeFacto",
                "slug": "defacto",
                "image": "https://ecommerce.routemisr.com/Route-Academy-brands/1678285758109.png"
              },
              "ratingsAverage": 4.1,
              "createdAt": "2023-04-02T02:43:18.400Z",
              "updatedAt": "2026-10-13T15:47:29.937Z",
              "id": "6428ebc6dc1175abc65ca0b9"
            },
            {
              "sold": 1103,
              "images": ["https://ecommerce.routemisr.com/Route-Academy-products/1680402411883-2.jpeg"],
              "ratingsQuantity": 15,
              "_id": "6428e7ecdc1175abc65ca090",
              "title": "Woman Bordeaux Long Sleeve Blouse BORDEAUX",
              "description": "ShellFabric1 Cotton 65% Polyester 35%",
              "price": 499,
              "priceAfterDiscount": 349,
              "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680402411833-cover.jpeg",
              "category": { "_id": "6439d58a0049ad0b52b9003f", "name": "Women's Fashion" },
              "brand": { "_id": "64089bbe24b25627a253158b", "name": "DeFacto" },
              "ratingsAverage": 4,
              "id": "6428e7ecdc1175abc65ca090"
            }
          ]
        }
    """.trimIndent()

    @Test
    fun `decodes the list payload ignoring unknown fields`() {
        val decoded = json.decodeFromString<ListResponse<ProductDto>>(listPayload)

        assertEquals(listOf("6428ebc6dc1175abc65ca0b9", "6428e7ecdc1175abc65ca090"), decoded.data.map { it.id })
        val shawl = decoded.data.first()
        assertEquals("Woman Shawl", shawl.title)
        assertEquals(149, shawl.price)
        assertEquals("DeFacto", shawl.brand?.name)
        assertEquals("6439d58a0049ad0b52b9003f", shawl.category?.id)
    }

    @Test
    fun `decodes the next page from the list metadata`() {
        val decoded = json.decodeFromString<ListResponse<ProductDto>>(listPayload)

        assertEquals(2, decoded.metadata?.nextPage)
    }

    @Test
    fun `a discounted product carries priceAfterDiscount and an integer rating decodes as a double`() {
        val decoded = json.decodeFromString<ListResponse<ProductDto>>(listPayload)

        val blouse = decoded.data[1]
        assertEquals(349, blouse.priceAfterDiscount)
        assertEquals(4.0, blouse.ratingsAverage!!, 0.0)
    }

    @Test
    fun `an undiscounted product has no priceAfterDiscount`() {
        val decoded = json.decodeFromString<ListResponse<ProductDto>>(listPayload)

        assertNull(decoded.data.first().priceAfterDiscount)
    }

    @Test
    fun `the last page has no nextPage`() {
        val payload = """
            {
              "results": 56,
              "metadata": { "currentPage": 28, "numberOfPages": 28, "limit": 2, "prevPage": 27 },
              "data": []
            }
        """.trimIndent()

        val decoded = json.decodeFromString<ListResponse<ProductDto>>(payload)

        assertNull(decoded.metadata?.nextPage)
    }

    @Test
    fun `a list without metadata still decodes`() {
        val decoded = json.decodeFromString<ListResponse<ProductDto>>("""{ "data": [] }""")

        assertNull(decoded.metadata)
    }

    @Test
    fun `missing optional fields decode to null instead of failing the list`() {
        val payload = """{ "data": [ { "_id": "p1", "title": "Bare", "price": 10 } ] }"""

        val product = json.decodeFromString<ListResponse<ProductDto>>(payload).data.single()

        assertEquals("p1", product.id)
        assertNull(product.imageCover)
        assertNull(product.images)
        assertNull(product.brand)
        assertNull(product.ratingsAverage)
    }

    @Test
    fun `decodes the details payload from the data envelope`() {
        val payload = """
            {
              "data": {
                "sold": 2841,
                "images": ["https://ecommerce.routemisr.com/Route-Academy-products/1680403397482-1.jpeg"],
                "subcategory": [{ "_id": "6407f1bcb575d3b90bf95797", "name": "Women's Clothing" }],
                "ratingsQuantity": 46,
                "_id": "6428ebc6dc1175abc65ca0b9",
                "title": "Woman Shawl",
                "description": "Material\tPolyester Blend",
                "quantity": 220,
                "price": 149,
                "imageCover": "https://ecommerce.routemisr.com/Route-Academy-products/1680403397402-cover.jpeg",
                "category": { "_id": "6439d58a0049ad0b52b9003f", "name": "Women's Fashion" },
                "brand": { "_id": "64089bbe24b25627a253158b", "name": "DeFacto" },
                "ratingsAverage": 4.1,
                "__v": 0,
                "reviews": [],
                "id": "6428ebc6dc1175abc65ca0b9"
              }
            }
        """.trimIndent()

        val product = json.decodeFromString<ItemResponse<ProductDto>>(payload).data

        assertEquals("6428ebc6dc1175abc65ca0b9", product.id)
        assertEquals(46, product.ratingsQuantity)
        assertEquals(2841, product.sold)
    }
}
