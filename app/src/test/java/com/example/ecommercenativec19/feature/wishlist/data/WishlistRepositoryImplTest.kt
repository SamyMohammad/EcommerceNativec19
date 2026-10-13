package com.example.ecommercenativec19.feature.wishlist.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.ListResponse
import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.products.data.ProductDto
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistRepositoryImplTest {

    private class FakeWishlistApi : WishlistApi {
        var wishlist: List<ProductDto> = emptyList()
        var failure: Exception? = null

        /** When set, every call suspends until it completes, so a test can look at the state mid-request. */
        var gate: CompletableDeferred<Unit>? = null

        val calls = mutableListOf<String>()

        private suspend fun enter(call: String) {
            calls.add(call)
            gate?.await()
            failure?.let { throw it }
        }

        override suspend fun getWishlist(): ListResponse<ProductDto> {
            enter("get")
            return ListResponse(wishlist)
        }

        override suspend fun add(request: AddToWishlistRequest): ListResponse<String> {
            enter("add:${request.productId}")
            return ListResponse(listOf(request.productId))
        }

        override suspend fun remove(productId: String): ListResponse<String> {
            enter("remove:$productId")
            return ListResponse(emptyList())
        }
    }

    private val api = FakeWishlistApi()
    private val session = FakeSessionRepository(SessionState.SignedIn("token"))

    private fun dto(id: String) = ProductDto(id = id, title = "Product $id", price = 100)

    private fun TestScope.repository() = WishlistRepositoryImpl(api, session, backgroundScope)

    @Test
    fun `a signed in session loads the favorite ids`() = runTest {
        api.wishlist = listOf(dto("a"), dto("b"))
        val repository = repository()

        runCurrent()

        assertEquals(setOf("a", "b"), repository.favoriteIds.value)
    }

    @Test
    fun `signing in later loads the favorite ids`() = runTest {
        api.wishlist = listOf(dto("a"))
        session.set(SessionState.SignedOut)
        val repository = repository()
        runCurrent()
        assertTrue(repository.favoriteIds.value.isEmpty())

        session.set(SessionState.SignedIn("token"))
        runCurrent()

        assertEquals(setOf("a"), repository.favoriteIds.value)
    }

    @Test
    fun `signing out clears the favorite ids`() = runTest {
        api.wishlist = listOf(dto("a"))
        val repository = repository()
        runCurrent()

        session.set(SessionState.SignedOut)
        runCurrent()

        assertTrue(repository.favoriteIds.value.isEmpty())
    }

    @Test
    fun `refresh returns the products and syncs the ids`() = runTest {
        val repository = repository()
        runCurrent()
        api.wishlist = listOf(dto("a"), dto("b"))

        val products = (repository.refresh() as AppResult.Success).data

        assertEquals(listOf("a", "b"), products.map { it.id })
        assertEquals(setOf("a", "b"), repository.favoriteIds.value)
    }

    @Test
    fun `refresh drops products the app cannot show`() = runTest {
        val repository = repository()
        runCurrent()
        api.wishlist = listOf(dto("a"), ProductDto(id = "broken"))

        val products = (repository.refresh() as AppResult.Success).data

        assertEquals(listOf("a"), products.map { it.id })
        assertEquals(setOf("a"), repository.favoriteIds.value)
    }

    @Test
    fun `a failed refresh reports the error and keeps the ids`() = runTest {
        api.wishlist = listOf(dto("a"))
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        val result = repository.refresh()

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertEquals(setOf("a"), repository.favoriteIds.value)
    }

    @Test
    fun `toggling a non favorite adds it before the server answers`() = runTest {
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()

        backgroundScope.launch { repository.toggle("a") }
        runCurrent()

        assertEquals(setOf("a"), repository.favoriteIds.value)
        assertEquals(listOf("get", "add:a"), api.calls)
    }

    @Test
    fun `toggling a favorite removes it before the server answers`() = runTest {
        api.wishlist = listOf(dto("a"))
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()

        backgroundScope.launch { repository.toggle("a") }
        runCurrent()

        assertTrue(repository.favoriteIds.value.isEmpty())
        assertEquals("remove:a", api.calls.last())
    }

    @Test
    fun `a successful toggle keeps the optimistic state`() = runTest {
        val repository = repository()
        runCurrent()

        val result = repository.toggle("a")

        assertEquals(AppResult.Success(Unit), result)
        assertEquals(setOf("a"), repository.favoriteIds.value)
    }

    @Test
    fun `a failed add rolls back`() = runTest {
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        val result = repository.toggle("a")

        assertEquals(AppResult.Failure(AppError.Network), result)
        assertTrue(repository.favoriteIds.value.isEmpty())
    }

    @Test
    fun `a failed remove rolls back`() = runTest {
        api.wishlist = listOf(dto("a"), dto("b"))
        val repository = repository()
        runCurrent()
        api.failure = IOException()

        repository.toggle("a")

        assertEquals(setOf("a", "b"), repository.favoriteIds.value)
    }

    @Test
    fun `a second tap while the first is in flight is ignored`() = runTest {
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        backgroundScope.launch { repository.toggle("a") }
        runCurrent()

        val second = repository.toggle("a")

        assertEquals(AppResult.Success(Unit), second)
        assertEquals(listOf("get", "add:a"), api.calls)
        assertEquals(setOf("a"), repository.favoriteIds.value)
    }

    @Test
    fun `different products can be toggled at the same time`() = runTest {
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()

        backgroundScope.launch { repository.toggle("a") }
        backgroundScope.launch { repository.toggle("b") }
        runCurrent()

        assertEquals(setOf("a", "b"), repository.favoriteIds.value)
    }

    @Test
    fun `a product can be toggled again once the first request finished`() = runTest {
        val repository = repository()
        runCurrent()

        repository.toggle("a")
        repository.toggle("a")

        assertTrue(repository.favoriteIds.value.isEmpty())
        assertEquals(listOf("get", "add:a", "remove:a"), api.calls)
    }

    @Test
    fun `a refresh response that predates a toggle does not undo it`() = runTest {
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        val refresh = backgroundScope.launch { repository.refresh() }
        runCurrent()

        // The toggle starts after the refresh request left, so the refresh result cannot contain it.
        backgroundScope.launch { repository.toggle("a") }
        runCurrent()
        api.gate?.complete(Unit)
        runCurrent()

        assertTrue(refresh.isCompleted)
        assertEquals(setOf("a"), repository.favoriteIds.value)
    }

    @Test
    fun `a rollback that lands after sign out does not bring the id back`() = runTest {
        api.wishlist = listOf(dto("a"))
        val repository = repository()
        runCurrent()
        api.gate = CompletableDeferred()
        api.failure = IOException()
        backgroundScope.launch { repository.toggle("a") }
        runCurrent()

        session.set(SessionState.SignedOut)
        runCurrent()
        api.gate?.complete(Unit)
        runCurrent()

        assertTrue(repository.favoriteIds.value.isEmpty())
    }
}
