package com.example.ecommercenativec19.feature.wishlist.di

import com.example.ecommercenativec19.feature.wishlist.WishlistViewModel
import com.example.ecommercenativec19.feature.wishlist.data.WishlistApi
import com.example.ecommercenativec19.feature.wishlist.data.WishlistRepositoryImpl
import com.example.ecommercenativec19.feature.wishlist.domain.GetWishlistUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.WishlistRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val wishlistModule = module {
    single<WishlistApi> { get<Retrofit>().create(WishlistApi::class.java) }
    // createdAtStart: begin observing the session at launch, so favorites are loaded before the first heart is drawn.
    single<WishlistRepository>(createdAtStart = true) { WishlistRepositoryImpl(get(), get(), get()) }
    factory { ObserveFavoriteIdsUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { GetWishlistUseCase(get()) }
    viewModelOf(::WishlistViewModel)
}
