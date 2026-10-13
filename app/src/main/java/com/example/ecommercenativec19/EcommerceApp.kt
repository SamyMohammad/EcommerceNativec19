package com.example.ecommercenativec19

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import com.example.ecommercenativec19.di.appModules
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class EcommerceApp : Application(), KoinComponent, SingletonImageLoader.Factory {
    // Resolved lazily on the first image request, after startKoin.
    private val okHttpClient: OkHttpClient by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EcommerceApp)
            modules(appModules)
        }
    }

    /** Reuses the app's OkHttpClient so image requests share its timeouts, logging and connection pool. */
    override fun newImageLoader(context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .components { add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })) }
            .crossfade(true)
            .build()
}
