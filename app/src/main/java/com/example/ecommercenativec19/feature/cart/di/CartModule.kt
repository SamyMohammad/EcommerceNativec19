package com.example.ecommercenativec19.feature.cart.di

import com.example.ecommercenativec19.feature.cart.CartViewModel
import com.example.ecommercenativec19.feature.cart.data.CartApi
import com.example.ecommercenativec19.feature.cart.data.CartRepositoryImpl
import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.CartRepository
import com.example.ecommercenativec19.feature.cart.domain.ObserveCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.RefreshCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.RemoveFromCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.UpdateCartItemCountUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val cartModule = module {
    single<CartApi> { get<Retrofit>().create(CartApi::class.java) }
    // createdAtStart: begin observing the session at launch, so the cart is loaded before the first screen asks.
    single<CartRepository>(createdAtStart = true) { CartRepositoryImpl(get(), get(), get()) }
    factory { ObserveCartUseCase(get()) }
    factory { RefreshCartUseCase(get()) }
    factory { AddToCartUseCase(get()) }
    factory { UpdateCartItemCountUseCase(get()) }
    factory { RemoveFromCartUseCase(get()) }
    viewModelOf(::CartViewModel)
}
