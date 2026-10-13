package com.example.ecommercenativec19.feature.wishlist.data

import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.safeApiCall
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.products.data.toDomain
import com.example.ecommercenativec19.feature.products.domain.Product
import com.example.ecommercenativec19.feature.wishlist.domain.WishlistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * App-scoped singleton: the cache must outlive every screen, or each ViewModel would start from an empty heart set.
 *
 * The cache follows the session: it is loaded when the user becomes signed in (so hearts are right before any screen
 * asks) and wiped on sign-out (so the next account never sees the previous one's favorites). A failed background
 * load stays silent: hearts show empty and the Wishlist screen's own load repopulates them.
 */
class WishlistRepositoryImpl(
    private val api: WishlistApi,
    private val session: SessionRepository,
    scope: CoroutineScope,
) : WishlistRepository {
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    override val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val lock = Any()

    /** Products with a request on the wire. A second tap on one of them is dropped, see [toggle]. */
    private val inFlight = mutableSetOf<String>()

    /** Bumped by every toggle and sign-out; lets [refresh] detect that its response is already out of date. */
    private var mutations = 0

    /** Bumped when the user signs in or out; a toggle that started under an older one belongs to nobody. */
    private var epoch = 0

    init {
        scope.launch {
            // Keyed on signed in / not, so a token rotation (password change) neither reloads nor bumps the epoch.
            // collectLatest: signing out cancels a load that was started for the previous session.
            session.state.map { it is SessionState.SignedIn }.distinctUntilChanged().collectLatest { signedIn ->
                synchronized(lock) {
                    epoch++
                    inFlight.clear()
                    if (!signedIn) {
                        mutations++
                        _favoriteIds.value = emptySet()
                    }
                }
                if (signedIn) refresh()
            }
        }
    }

    override suspend fun refresh(): AppResult<List<Product>> {
        val versionAtStart = synchronized(lock) { mutations }
        return when (val result = safeApiCall { api.getWishlist().data }) {
            is AppResult.Failure -> result
            is AppResult.Success -> {
                val products = result.data.mapNotNull { it.toDomain() }
                synchronized(lock) {
                    // A toggle that ran meanwhile is newer than this response; overwriting would undo it.
                    if (mutations == versionAtStart) _favoriteIds.value = products.mapTo(HashSet()) { it.id }
                }
                AppResult.Success(products)
            }
        }
    }

    override suspend fun toggle(productId: String): AppResult<Unit> {
        // Why drop instead of queue: two overlapping POST/DELETE for one product can reach the server out of order, and
        // there would be two rollbacks competing over one id. One request per product keeps both simple.
        val (wasFavorite, startEpoch) = synchronized(lock) {
            if (!inFlight.add(productId)) return AppResult.Success(Unit)
            mutations++
            (productId in _favoriteIds.value).also { setFavorite(productId, !it) } to epoch
        }
        // NonCancellable: leaving the screen right after the tap must not abandon the request, or the cache would show
        // a state the server never received.
        return withContext(NonCancellable) {
            val result = safeApiCall { if (wasFavorite) api.remove(productId) else api.add(AddToWishlistRequest(productId)) }
            synchronized(lock) {
                // After a sign-out the cache and inFlight already belong to the next session; touching either would
                // resurrect an old id or drop a new account's tap.
                if (epoch == startEpoch) {
                    inFlight.remove(productId)
                    if (result is AppResult.Failure && session.state.value is SessionState.SignedIn) {
                        setFavorite(productId, wasFavorite)
                    }
                }
            }
            when (result) {
                is AppResult.Success -> AppResult.Success(Unit)
                is AppResult.Failure -> result
            }
        }
    }

    private fun setFavorite(productId: String, favorite: Boolean) {
        _favoriteIds.update { if (favorite) it + productId else it - productId }
    }
}
