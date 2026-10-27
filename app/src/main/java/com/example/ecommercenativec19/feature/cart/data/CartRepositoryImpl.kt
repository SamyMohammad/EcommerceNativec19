package com.example.ecommercenativec19.feature.cart.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.di.ApplicationScope
import com.example.ecommercenativec19.core.network.safeApiCall
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.cart.domain.Cart
import com.example.ecommercenativec19.feature.cart.domain.CartRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * App-scoped singleton, like the wishlist cache: it must outlive every screen, and it follows the session (loaded on
 * sign-in, wiped on sign-out).
 *
 * The cache shown to the UI ([cart]) is derived from two things: [confirmed], the last cart the server sent, and
 * [pending], the counts the user has asked for that the server has not confirmed yet. Because the display is always
 * "confirmed + pending", a server answer can replace [confirmed] at any time without undoing taps on other
 * products, and a rollback is just dropping a pending entry.
 *
 * Why coalesce instead of drop (the wishlist drops): a heart is a flip, so a second tap during a request is
 * ambiguous; a quantity tap is "one more", so ten quick taps mean 10 and dropping would lose nine of them. One worker
 * per product sends the latest wanted count, then checks whether the user asked for something newer meanwhile.
 */
class CartRepositoryImpl @Inject constructor(
    private val api: CartApi,
    private val session: SessionRepository,
    @param:ApplicationScope private val scope: CoroutineScope,
) : CartRepository {
    private val _cart = MutableStateFlow<Cart?>(null)
    override val cart: StateFlow<Cart?> = _cart.asStateFlow()

    private val lock = Any()

    // Everything below is guarded by [lock].
    private var confirmed: Cart? = null

    /** Wanted count per product, 0 meaning "remove". Present only while a worker still has to confirm it. */
    private val pending = mutableMapOf<String, Int>()
    private val workers = mutableSetOf<WorkerKey>()

    /** Bumped when the session changes; a response that started under an older epoch belongs to nobody. */
    private var epoch = 0

    /** Bumped by every mutation (start and answer); lets [refresh] detect that its response is already out of date. */
    private var mutations = 0

    /** Keeps a product's add (POST then PUT) and its count worker from overlapping on the wire. */
    private val productLocks = ConcurrentHashMap<String, Mutex>()

    private data class WorkerKey(val epoch: Int, val productId: String)

    init {
        scope.launch {
            // Keyed on signed in / not, not on every emission: a password change swaps the token for the same user and
            // must not wipe the cart. An account switch always passes through SignedOut (sign-in is only reachable
            // while signed out), so a token change inside SignedIn is always a rotation.
            // collectLatest: signing out cancels a load that was started for the previous session.
            session.state.map { it is SessionState.SignedIn }.distinctUntilChanged().collectLatest { signedIn ->
                resetForNewSession()
                if (signedIn) refresh()
            }
        }
    }

    override suspend fun refresh(): AppResult<Unit> {
        val (startEpoch, startMutations) = synchronized(lock) { epoch to mutations }
        return when (val result = safeApiCall { api.getCart() }) {
            is AppResult.Failure -> result
            is AppResult.Success -> {
                val fetched = result.data.toDomain()
                synchronized(lock) {
                    // A mutation that ran meanwhile is newer than this response; overwriting would undo it.
                    val stale = confirmed != null && (mutations != startMutations || pending.isNotEmpty())
                    if (epoch == startEpoch && !stale) {
                        confirmed = fetched
                        render()
                    }
                }
                AppResult.Success(Unit)
            }
        }
    }

    override suspend fun addProduct(productId: String, quantity: Int): AppResult<Unit> {
        require(quantity >= 1) { "quantity must be at least 1" }
        val startEpoch = synchronized(lock) {
            mutations++
            epoch
        }
        // NonCancellable: leaving the screen right after the tap must not abandon a request the server may already have.
        return withContext(NonCancellable) {
            productLocks.getOrPut(productId) { Mutex() }.withLock { add(productId, quantity, startEpoch) }
        }
    }

    private suspend fun add(productId: String, quantity: Int, startEpoch: Int): AppResult<Unit> {
        // This add may have waited for the product lock across a sign-out; its POST would carry the next session's token.
        if (synchronized(lock) { epoch != startEpoch }) return AppResult.Failure(AppError.Unknown)
        val added = when (val result = safeApiCall { api.add(AddToCartRequest(productId)) }) {
            is AppResult.Failure -> return result
            is AppResult.Success -> result.data
        }
        // POST adds exactly one unit and answers with bare product ids, so it cannot fill in the line's title and
        // image. A second request does that: PUT when more than one unit was wanted, GET otherwise.
        val countNow = added.data.products.firstOrNull { it.product?.id == productId }?.count ?: 1
        val wanted = countNow + quantity - 1
        val filledIn = safeApiCall {
            if (wanted > countNow) api.updateCount(productId, UpdateCartCountRequest(wanted)) else api.getCart()
        }
        return when (filledIn) {
            is AppResult.Failure -> {
                // The server already holds the first unit; resync so the cache does not claim otherwise.
                scope.launch { refresh() }
                filledIn
            }
            is AppResult.Success -> {
                synchronized(lock) {
                    if (epoch == startEpoch) {
                        mutations++
                        confirmed = filledIn.data.toDomain()
                        render()
                    }
                }
                AppResult.Success(Unit)
            }
        }
    }

    override suspend fun updateCount(productId: String, count: Int): AppResult<Unit> {
        require(count >= 1) { "count must be at least 1, use remove()" }
        return setCount(productId, count)
    }

    override suspend fun remove(productId: String): AppResult<Unit> = setCount(productId, REMOVE)

    private suspend fun setCount(productId: String, target: Int): AppResult<Unit> {
        val ticket = synchronized(lock) {
            // Only products the user can see have a count to change; this also covers a not yet loaded cart.
            if (confirmed?.items?.any { it.productId == productId } != true) return AppResult.Success(Unit)
            mutations++
            pending[productId] = target
            render()
            // A worker already on the wire for this product will pick up the new target; this call just returns.
            WorkerKey(epoch, productId).takeIf { workers.add(it) }
        } ?: return AppResult.Success(Unit)
        return withContext(NonCancellable) {
            productLocks.getOrPut(productId) { Mutex() }.withLock { drain(ticket) }
        }
    }

    /** Sends the wanted count until the server has confirmed the latest one, then settles the cache. */
    private suspend fun drain(worker: WorkerKey): AppResult<Unit> {
        val productId = worker.productId
        while (true) {
            val target = synchronized(lock) {
                if (epoch != worker.epoch) {
                    workers.remove(worker)
                    return AppResult.Success(Unit)
                }
                pending.getValue(productId)
            }
            val result = safeApiCall {
                if (target == REMOVE) api.remove(productId) else api.updateCount(productId, UpdateCartCountRequest(target))
            }
            val finished: AppResult<Unit>? = synchronized(lock) {
                when {
                    // After a sign-out the cache already belongs to nobody; touching it would resurrect old data.
                    epoch != worker.epoch -> {
                        workers.remove(worker)
                        result.asUnit()
                    }
                    result is AppResult.Failure -> {
                        pending.remove(productId)
                        workers.remove(worker)
                        render()
                        result
                    }
                    else -> {
                        mutations++
                        confirmed = (result as AppResult.Success).data.toDomain()
                        if (pending[productId] == target) {
                            pending.remove(productId)
                            workers.remove(worker)
                            render()
                            AppResult.Success(Unit)
                        } else {
                            // The user tapped again while this request was out: keep the display, send the newer count.
                            render()
                            null
                        }
                    }
                }
            }
            if (finished != null) return finished
        }
    }

    private fun resetForNewSession() {
        synchronized(lock) {
            epoch++
            mutations++
            confirmed = null
            pending.clear()
            render()
        }
    }

    /** Must be called with [lock] held. */
    private fun render() {
        _cart.value = confirmed?.withPending()
    }

    private fun Cart.withPending(): Cart {
        if (pending.isEmpty()) return this
        val lines = items.mapNotNull { line ->
            when (val wanted = pending[line.productId]) {
                null -> line
                REMOVE -> null
                else -> line.copy(count = wanted)
            }
        }
        // The only place the app does its own sums, and only until the server's figure arrives.
        return Cart(items = lines, totalPrice = lines.sumOf { it.linePrice })
    }

    private fun <T> AppResult<T>.asUnit(): AppResult<Unit> = when (this) {
        is AppResult.Success -> AppResult.Success(Unit)
        is AppResult.Failure -> this
    }

    private companion object {
        const val REMOVE = 0
    }
}
