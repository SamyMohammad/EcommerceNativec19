package com.example.ecommercenativec19.feature.cart.di

import com.example.ecommercenativec19.feature.cart.data.CartApi
import com.example.ecommercenativec19.feature.cart.data.CartRepositoryImpl
import com.example.ecommercenativec19.feature.cart.domain.CartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class CartModule {
    // Eagerly created (EcommerceApp injects it): begin observing the session at launch, so the cart is loaded before the first screen asks.
    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    companion object {
        @Provides
        @Singleton
        fun provideCartApi(retrofit: Retrofit): CartApi = retrofit.create(CartApi::class.java)
    }
}
