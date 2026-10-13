package com.example.ecommercenativec19.feature.cart.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartMapperTest {
    private val json = Json { ignoreUnknownKeys = true }

    private fun decode(body: String) = json.decodeFromString<CartResponse>(body)

    @Test
    fun `a populated product maps to a line`() {
        val cart = decode(
            """{"status":"success","numOfCartItems":1,"cartId":"c","data":{"_id":"c","products":[
              {"count":3,"_id":"l","price":149,"product":{"_id":"p1","id":"p1","title":" Shawl ","imageCover":"https://x/y.jpeg",
               "quantity":220,"category":{"_id":"k","name":"Women"}}}],"totalCartPrice":447}}""",
        ).toDomain()

        val line = cart.items.single()
        assertEquals("p1", line.productId)
        assertEquals("Shawl", line.title)
        assertEquals("https://x/y.jpeg", line.imageUrl)
        assertEquals(149, line.unitPrice)
        assertEquals(3, line.count)
        assertEquals(447, cart.totalPrice)
        assertEquals(3, cart.itemCount)
    }

    @Test
    fun `a product sent as a bare id decodes but is not shown`() {
        val response = decode(
            """{"status":"success","numOfCartItems":1,"data":{"products":[{"count":1,"_id":"l","product":"p1","price":149}],"totalCartPrice":149}}""",
        )

        assertEquals("p1", response.data.products.single().product?.id)
        assertTrue(response.toDomain().items.isEmpty())
        assertEquals(149, response.toDomain().totalPrice)
    }

    @Test
    fun `an empty cart has no lines and a zero total`() {
        val cart = decode("""{"status":"success","numOfCartItems":0,"data":{"products":[],"totalCartPrice":0}}""").toDomain()

        assertTrue(cart.items.isEmpty())
        assertEquals(0, cart.totalPrice)
    }

    @Test
    fun `lines without a title, count or price are dropped`() {
        val cart = decode(
            """{"data":{"products":[
              {"count":1,"price":10,"product":{"_id":"no-title"}},
              {"count":0,"price":10,"product":{"_id":"zero","title":"T"}},
              {"count":1,"product":{"_id":"no-price","title":"T"}},
              {"count":2,"price":10,"product":{"_id":"ok","title":"T"}}],"totalCartPrice":20}}""",
        ).toDomain()

        assertEquals(listOf("ok"), cart.items.map { it.productId })
    }

    @Test
    fun `a missing total falls back to the sum of the lines`() {
        val cart = decode("""{"data":{"products":[{"count":2,"price":10,"product":{"_id":"a","title":"T"}}]}}""").toDomain()

        assertEquals(20, cart.totalPrice)
    }
}
