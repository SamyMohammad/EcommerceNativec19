package com.example.ecommercenativec19

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.feature.cart.domain.CartRepository
import com.example.ecommercenativec19.feature.wishlist.domain.WishlistRepository
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import okhttp3.OkHttpClient

@HiltAndroidApp
class EcommerceApp : Application(), SingletonImageLoader.Factory {
    // Lazy so the image loader factory never forces DI resolution itself; the eager repositories below build the client anyway.
    @Inject lateinit var okHttpClient: Lazy<OkHttpClient>

    // Injected only so they start at launch, warming the session, cart and favorites before screens ask.
    @Inject lateinit var sessionRepository: SessionRepository
    @Inject lateinit var cartRepository: CartRepository
    @Inject lateinit var wishlistRepository: WishlistRepository

    /** Reuses the app's OkHttpClient so image requests share its timeouts, logging and connection pool. */
    override fun newImageLoader(context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .components { add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient.get() })) }
            .crossfade(true)
            .build()
}
