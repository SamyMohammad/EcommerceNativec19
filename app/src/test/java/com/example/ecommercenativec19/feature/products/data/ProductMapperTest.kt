package com.example.ecommercenativec19.feature.products.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductMapperTest {

    private fun dto(
        id: String? = "p1",
        title: String? = "Woman Shawl",
        price: Int? = 149,
        priceAfterDiscount: Int? = null,
        imageCover: String? = "https://img.test/cover.jpg",
        images: List<String>? = listOf("https://img.test/1.jpg", "https://img.test/2.jpg"),
        ratingsAverage: Double? = 4.1,
        ratingsQuantity: Int? = 46,
        sold: Int? = 2841,
    ) = ProductDto(
        id = id,
        title = title,
        description = "  Polyester Blend  ",
        price = price,
        priceAfterDiscount = priceAfterDiscount,
        imageCover = imageCover,
        images = images,
        ratingsAverage = ratingsAverage,
        ratingsQuantity = ratingsQuantity,
        sold = sold,
        category = ProductCategoryDto(id = "cat-1"),
        brand = ProductBrandDto(name = "DeFacto"),
    )

    @Test
    fun `maps the scalar fields`() {
        val product = dto().toDomain()!!

        assertEquals("p1", product.id)
        assertEquals("Woman Shawl", product.title)
        assertEquals(149, product.price)
        assertEquals(4.1, product.rating, 0.0)
        assertEquals(46, product.ratingCount)
        assertEquals(2841, product.soldCount)
    }

    @Test
    fun `maps category id and brand name`() {
        val product = dto().toDomain()!!

        assertEquals("cat-1", product.categoryId)
        assertEquals("DeFacto", product.brandName)
    }

    @Test
    fun `trims the description`() {
        assertEquals("Polyester Blend", dto().toDomain()!!.description)
    }

    @Test
    fun `uses the cover as the card image`() {
        assertEquals("https://img.test/cover.jpg", dto().toDomain()!!.imageUrl)
    }

    @Test
    fun `falls back to the first gallery image when the cover is missing`() {
        assertEquals("https://img.test/1.jpg", dto(imageCover = null).toDomain()!!.imageUrl)
    }

    @Test
    fun `keeps the gallery images in order`() {
        assertEquals(listOf("https://img.test/1.jpg", "https://img.test/2.jpg"), dto().toDomain()!!.images)
    }

    @Test
    fun `drops blank gallery entries`() {
        val product = dto(images = listOf("", "https://img.test/1.jpg", "  ")).toDomain()!!

        assertEquals(listOf("https://img.test/1.jpg"), product.images)
    }

    @Test
    fun `a product without a gallery shows its cover as the only image`() {
        val product = dto(images = null).toDomain()!!

        assertEquals(listOf("https://img.test/cover.jpg"), product.images)
    }

    @Test
    fun `a product without any image has none`() {
        val product = dto(imageCover = null, images = null).toDomain()!!

        assertNull(product.imageUrl)
        assertEquals(emptyList<String>(), product.images)
    }

    @Test
    fun `keeps a real discount`() {
        val product = dto(price = 499, priceAfterDiscount = 349).toDomain()!!

        assertEquals(349, product.priceAfterDiscount)
        assertEquals(349, product.currentPrice)
    }

    @Test
    fun `current price is the price when there is no discount`() {
        assertEquals(149, dto().toDomain()!!.currentPrice)
    }

    @Test
    fun `ignores a discount that is not below the price`() {
        assertNull(dto(price = 100, priceAfterDiscount = 100).toDomain()!!.priceAfterDiscount)
    }

    @Test
    fun `ignores a negative discount price`() {
        assertNull(dto(price = 100, priceAfterDiscount = -5).toDomain()!!.priceAfterDiscount)
    }

    @Test
    fun `missing rating fields default to zero`() {
        val product = dto(ratingsAverage = null, ratingsQuantity = null, sold = null).toDomain()!!

        assertEquals(0.0, product.rating, 0.0)
        assertEquals(0, product.ratingCount)
        assertEquals(0, product.soldCount)
    }

    @Test
    fun `clamps an out of range rating`() {
        assertEquals(5.0, dto(ratingsAverage = 9.0).toDomain()!!.rating, 0.0)
    }

    @Test
    fun `skips a product without an id`() {
        assertNull(dto(id = null).toDomain())
    }

    @Test
    fun `skips a product with a blank id`() {
        assertNull(dto(id = "  ").toDomain())
    }

    @Test
    fun `skips a product without a title`() {
        assertNull(dto(title = " ").toDomain())
    }

    @Test
    fun `skips a product without a price`() {
        assertNull(dto(price = null).toDomain())
    }

    @Test
    fun `skips a product with a negative price`() {
        assertNull(dto(price = -1).toDomain())
    }
}
