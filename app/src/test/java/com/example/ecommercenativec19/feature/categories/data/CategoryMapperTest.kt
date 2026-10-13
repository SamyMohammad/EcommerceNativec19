package com.example.ecommercenativec19.feature.categories.data

import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryMapperTest {

    @Test
    fun `maps CategoryDto fields to Category domain model`() {
        val dto = CategoryDto(id = "cat-1", name = "Men's Fashion", image = "https://example.com/img.jpg")

        val domain = dto.toDomain()

        assertEquals("cat-1", domain.id)
        assertEquals("Men's Fashion", domain.name)
        assertEquals("https://example.com/img.jpg", domain.imageUrl)
    }

    @Test
    fun `maps SubCategoryDto fields to SubCategory domain model`() {
        val dto = SubCategoryDto(id = "sub-1", name = "Men's Clothing")

        val domain = dto.toDomain()

        assertEquals("sub-1", domain.id)
        assertEquals("Men's Clothing", domain.name)
    }
}
