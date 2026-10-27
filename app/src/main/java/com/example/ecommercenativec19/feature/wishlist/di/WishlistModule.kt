package com.example.ecommercenativec19.feature.wishlist.di

import com.example.ecommercenativec19.feature.wishlist.data.WishlistApi
import com.example.ecommercenativec19.feature.wishlist.data.WishlistRepositoryImpl
import com.example.ecommercenativec19.feature.wishlist.domain.WishlistRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class WishlistModule {
    // Eagerly created (EcommerceApp injects it): begin observing the session at launch, so favorites are loaded before the first heart is drawn.
    @Binds
    @Singleton
    abstract fun bindWishlistRepository(impl: WishlistRepositoryImpl): WishlistRepository

    companion object {
        @Provides
        @Singleton
        fun provideWishlistApi(retrofit: Retrofit): WishlistApi = retrofit.create(WishlistApi::class.java)
    }
}
