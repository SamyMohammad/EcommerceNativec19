package com.example.ecommercenativec19.feature.categories.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubCategoryDto(
    @SerialName("_id") val id: String,
    val name: String,
)
