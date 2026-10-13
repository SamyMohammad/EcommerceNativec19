package com.example.ecommercenativec19.feature.cart.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {

    /** A tiny server: it keeps the cart, charges [UNIT_PRICE] per unit and answers like the Route API (docs/api/cart.md). */
    private class FakeCartApi : CartApi {
        val lines = linkedMapOf<String, Int>()

        /** Added to every total, so a test can tell the server's figure from the app's own multiplication. */
        var totalAdjustment = 0
        var failure: Exception? = null

        /** When set, only calls whose label starts with this fail. */
        var failOnly: String? = null

        /** When set, every call suspends until it completes, so a test can look at the state mid-request. */
        var gate: CompletableDeferred<Unit>? = null

        val calls = mutableListOf<String>()

        private suspend fun enter(call: String) {
            calls.add(call)
            gate?.await()
            val only = failOnly
            if (only == null || call.startsWith(only)) failure?.let { throw it }
        }

        /** POST answers with bare product ids; the other endpoints with populated products. */
        private fun response(populated: Boolean) = CartResponse(
            CartDto(
                products = lines.map { (id, count) ->
                    CartLineDto(
                        count = count,
                        price = UNIT_PRICE,
                        product = CartProductDto(
                            id = id,
                            title = if (populated) "Product $id" else null,
                            imageCover = if (populated) "img-$id" else null,
                        ),
                    )
                },
                totalCartPrice = lines.values.sum() * UNIT_PRICE + totalAdjustment,
            ),
        )

        override suspend fun getCart(): CartResponse {
            enter("get")
            return response(populated = true)
        }

        override suspend fun add(request: AddToCartRequest): CartResponse {
            enter("add:${request.productId}")
            lines[request.productId] = (lines[request.productId] ?: 0) + 1
            return response(populated = false)
        }

        override suspend fun updateCount(productId: String, request: UpdateCartCountRequest): CartResponse {
            enter("put:$productId:${request.count}")
            lines[productId] = request.count
            return response(populated = true)
        }

        override suspend fun remove(productId: String): CartResponse {
            enter("delete:$productId")
            lines.remove(productId)
            return response(populated = true)
        }
    }

    private val api = FakeCartApi()
    private val session = FakeSessionRepository(SessionState.SignedIn("token"))

    private fun TestScope.repository() = CartRepositoryImpl(api, session, backgroundScope)

    private fun CartRepositoryImpl.counts() = cart.value?.items?.associate { it.productId to it.count }

    @Test
    fun `a signed in session loads the cart`() = runTest {
        api.lines["a"] = 2
        val repository = repository()

        runCurrent()

        assertEquals(mapOf("a" to 2), repository.counts())
        assertEquals(2 * UNIT_PRICE, repository.cart.value?.totalPrice)
    }

    @Test
    fun `signing in later loads the cart`() = runTest {
        api.lines["a"] = 1
        session.set(SessionState.SignedOut)
        val repository = repository()
        runCurrent()
        assertNull(repository.cart.value)

        session.set(SessionState.SignedIn("token"))
        runCurrent()

        assertEquals(mapOf("a" to 1), repository.counts())
    }

    @Test
    fun `signing out clears the cart`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()

        session.set(SessionState.SignedOut)
        runCurrent()

        assertNull(repository.cart.value)
    }

    @Test
    fun `a token rotation for the same session keeps the cart and its pending counts`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        backgroundScope.launch { repository.updateCount("a", 3) }
        runCurrent()
        assertEquals(mapOf("a" to 3), repository.counts())

        session.set(SessionState.SignedIn("rotated-token"))
        runCurrent()
        assertEquals(mapOf("a" to 3), repository.counts())
        api.gate?.complete(Unit)
        runCurrent()

        assertEquals(mapOf("a" to 3), repository.counts())
        assertEquals(listOf("get", "put:a:3"), api.calls)
    }

    @Test
    fun `an add queued behind another one sends nothing after the session changed`() = runTest {
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        backgroundScope.launch { repository.addProduct("a") }
        runCurrent()
        val queued = backgroundScope.launch { repository.addProduct("a") }
        runCurrent()

        session.set(SessionState.SignedOut)
        runCurrent()
        session.set(SessionState.SignedIn("other-token"))
        runCurrent()
        api.gate?.complete(Unit)
        runCurrent()

        assertTrue(queued.isCompleted)
        assertEquals(1, api.calls.count { it == "add:a" })
    }

    @Test
    fun `a failed refresh reports the error and keeps the cart`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        val result = repository.refresh()

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertEquals(mapOf("a" to 1), repository.counts())
    }

    @Test
    fun `a refresh picks up changes made on another device`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.lines["a"] = 4

        repository.refresh()

        assertEquals(mapOf("a" to 4), repository.counts())
    }

    @Test
    fun `a new count shows before the server answers, with a locally summed total`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()

        backgroundScope.launch { repository.updateCount("a", 3) }
        runCurrent()

        assertEquals(mapOf("a" to 3), repository.counts())
        assertEquals(3 * UNIT_PRICE, repository.cart.value?.totalPrice)
    }

    @Test
    fun `once the server answers its total replaces the local one`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.totalAdjustment = 7

        val result = repository.updateCount("a", 3)

        assertEquals(AppResult.Success(Unit), result)
        assertEquals(mapOf("a" to 3), repository.counts())
        assertEquals(3 * UNIT_PRICE + 7, repository.cart.value?.totalPrice)
    }

    @Test
    fun `a failed update restores the confirmed count and total`() = runTest {
        api.lines["a"] = 2
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        val result = repository.updateCount("a", 5)

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertEquals(mapOf("a" to 2), repository.counts())
        assertEquals(2 * UNIT_PRICE, repository.cart.value?.totalPrice)
    }

    @Test
    fun `taps while a request is out are coalesced into one request with the latest count`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        backgroundScope.launch { repository.updateCount("a", 2) }
        runCurrent()

        val second = repository.updateCount("a", 3)
        val third = repository.updateCount("a", 4)
        assertEquals(AppResult.Success(Unit), second)
        assertEquals(AppResult.Success(Unit), third)
        assertEquals(mapOf("a" to 4), repository.counts())
        api.gate?.complete(Unit)
        runCurrent()

        assertEquals(listOf("get", "put:a:2", "put:a:4"), api.calls)
        assertEquals(mapOf("a" to 4), repository.counts())
        assertEquals(4 * UNIT_PRICE, repository.cart.value?.totalPrice)
    }

    @Test
    fun `a failure after coalesced taps rolls everything back to the confirmed count`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        api.failure = IOException()
        backgroundScope.launch { repository.updateCount("a", 2) }
        runCurrent()
        repository.updateCount("a", 3)

        api.gate?.complete(Unit)
        runCurrent()

        assertEquals(mapOf("a" to 1), repository.counts())
    }

    @Test
    fun `different products update at the same time`() = runTest {
        api.lines["a"] = 1
        api.lines["b"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()

        backgroundScope.launch { repository.updateCount("a", 2) }
        backgroundScope.launch { repository.updateCount("b", 5) }
        runCurrent()

        assertEquals(mapOf("a" to 2, "b" to 5), repository.counts())
        assertEquals(listOf("get", "put:a:2", "put:b:5"), api.calls)
    }

    @Test
    fun `an answer for one product keeps the unconfirmed count of another`() = runTest {
        api.lines["a"] = 1
        api.lines["b"] = 1
        val repository = repository()
        runCurrent()
        val gateB = CompletableDeferred<Unit>()
        api.gate = gateB
        backgroundScope.launch { repository.updateCount("b", 9) }
        runCurrent()
        // "a" is served without waiting: the gate only holds the call that is already inside it.
        api.gate = null

        repository.updateCount("a", 2)

        assertEquals(mapOf("a" to 2, "b" to 9), repository.counts())
        gateB.complete(Unit)
        runCurrent()
        assertEquals(mapOf("a" to 2, "b" to 9), repository.counts())
    }

    @Test
    fun `remove drops the line at once and the server confirms`() = runTest {
        api.lines["a"] = 1
        api.lines["b"] = 2
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()

        backgroundScope.launch { repository.remove("a") }
        runCurrent()
        assertEquals(mapOf("b" to 2), repository.counts())
        api.gate?.complete(Unit)
        runCurrent()

        assertEquals("delete:a", api.calls.last())
        assertEquals(mapOf("b" to 2), repository.counts())
        assertEquals(2 * UNIT_PRICE, repository.cart.value?.totalPrice)
    }

    @Test
    fun `a failed remove brings the line back`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        repository.remove("a")

        assertEquals(mapOf("a" to 1), repository.counts())
    }

    @Test
    fun `changing a product that is not in the cart sends nothing`() = runTest {
        val repository = repository()
        runCurrent()

        val update = repository.updateCount("ghost", 2)
        val remove = repository.remove("ghost")

        assertEquals(AppResult.Success(Unit), update)
        assertEquals(AppResult.Success(Unit), remove)
        assertEquals(listOf("get"), api.calls)
    }

    @Test
    fun `adding a new product loads its title from the server`() = runTest {
        val repository = repository()
        runCurrent()

        val result = repository.addProduct("a")

        assertEquals(AppResult.Success(Unit), result)
        assertEquals(listOf("get", "add:a", "get"), api.calls)
        val line = repository.cart.value?.items?.single()
        assertEquals("Product a", line?.title)
        assertEquals(1, line?.count)
    }

    @Test
    fun `adding several units sets the count after the first unit`() = runTest {
        val repository = repository()
        runCurrent()

        repository.addProduct("a", quantity = 3)

        assertEquals(listOf("get", "add:a", "put:a:3"), api.calls)
        assertEquals(mapOf("a" to 3), repository.counts())
        assertEquals(3 * UNIT_PRICE, repository.cart.value?.totalPrice)
    }

    @Test
    fun `adding on top of an existing line adds to its count`() = runTest {
        api.lines["a"] = 2
        val repository = repository()
        runCurrent()

        repository.addProduct("a", quantity = 2)

        assertEquals(mapOf("a" to 4), repository.counts())
    }

    @Test
    fun `a failed add changes nothing`() = runTest {
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        val result = repository.addProduct("a")

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertEquals(emptyMap<String, Int>(), repository.counts())
    }

    @Test
    fun `when only the second step of an add fails the cart is resynced`() = runTest {
        val repository = repository()
        runCurrent()
        api.failure = IOException()
        api.failOnly = "get"

        val result = repository.addProduct("a")
        // The resync GET fails too (failOnly still matches), so let it through before looking.
        api.failure = null
        runCurrent()
        repository.refresh()

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertEquals(mapOf("a" to 1), repository.counts())
    }

    @Test
    fun `a refresh response that predates a change does not undo it`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        val refresh = backgroundScope.launch { repository.refresh() }
        runCurrent()

        // The change starts after the refresh request left, so the refresh result cannot contain it.
        backgroundScope.launch { repository.updateCount("a", 5) }
        runCurrent()
        api.gate?.complete(Unit)
        runCurrent()

        assertEquals(true, refresh.isCompleted)
        assertEquals(mapOf("a" to 5), repository.counts())
    }

    @Test
    fun `an answer that lands after sign out does not bring the cart back`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        backgroundScope.launch { repository.updateCount("a", 3) }
        runCurrent()

        session.set(SessionState.SignedOut)
        runCurrent()
        api.gate?.complete(Unit)
        runCurrent()

        assertNull(repository.cart.value)
    }

    @Test
    fun `a rollback that lands after sign out does not bring the cart back`() = runTest {
        api.lines["a"] = 1
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        api.failure = IOException()
        backgroundScope.launch { repository.updateCount("a", 3) }
        runCurrent()

        session.set(SessionState.SignedOut)
        runCurrent()
        api.gate?.complete(Unit)
        runCurrent()

        assertNull(repository.cart.value)
    }

    private companion object {
        const val UNIT_PRICE = 100
    }
}
