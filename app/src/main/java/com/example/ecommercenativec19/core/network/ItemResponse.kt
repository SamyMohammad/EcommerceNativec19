package com.example.ecommercenativec19.core.network

import kotlinx.serialization.Serializable

/** Envelope of the single-item endpoints: `{ "data": { ... } }`. */
@Serializable
data class ItemResponse<T>(val data: T)
