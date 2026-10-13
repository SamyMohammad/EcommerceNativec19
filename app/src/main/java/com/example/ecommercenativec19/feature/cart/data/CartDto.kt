package com.example.ecommercenativec19.feature.cart.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

/** Envelope shared by all four endpoints; `status`, `numOfCartItems` (a line count) and `cartId` are not needed. */
@Serializable
data class CartResponse(val data: CartDto)

@Serializable
data class CartDto(
    val products: List<CartLineDto> = emptyList(),
    /** Whole EGP; the server's figure, shown as is. */
    val totalCartPrice: Int? = null,
)

/** Everything is nullable: the mapper, not the decoder, decides what a usable line is. */
@Serializable
data class CartLineDto(
    val count: Int? = null,
    /** Unit price at the time the product was added. */
    val price: Int? = null,
    @Serializable(with = CartProductSerializer::class) val product: CartProductDto? = null,
)

@Serializable
data class CartProductDto(
    @SerialName("_id") val id: String? = null,
    val title: String? = null,
    val imageCover: String? = null,
)

/**
 * POST answers with `"product": "<id>"` while GET, PUT and DELETE answer with the product object. A bare id is read as
 * an object that has only an `_id`, so the rest of the app sees one shape.
 */
object CartProductSerializer : JsonTransformingSerializer<CartProductDto>(CartProductDto.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive && element !is JsonNull) JsonObject(mapOf("_id" to element)) else element
}
