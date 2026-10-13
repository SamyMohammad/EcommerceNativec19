package com.example.ecommercenativec19.feature.categories.data

import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.SubCategory

internal fun CategoryDto.toDomain() = Category(id = id, name = name, imageUrl = image)

internal fun SubCategoryDto.toDomain() = SubCategory(id = id, name = name)
