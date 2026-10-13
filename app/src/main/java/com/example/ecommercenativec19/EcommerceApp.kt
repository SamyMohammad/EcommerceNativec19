package com.example.ecommercenativec19

import android.app.Application
import com.example.ecommercenativec19.core.network.networkModule
import com.example.ecommercenativec19.feature.categories.di.categoriesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EcommerceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EcommerceApp)
            modules(networkModule, categoriesModule)
        }
    }
}
