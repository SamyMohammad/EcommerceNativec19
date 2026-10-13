package com.example.ecommercenativec19.feature.categories.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("_id") val id: String,
    val name: String,
    val image: String? = null,
)
