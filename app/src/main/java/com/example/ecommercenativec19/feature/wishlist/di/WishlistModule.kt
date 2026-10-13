package com.example.ecommercenativec19.feature.wishlist.di

import com.example.ecommercenativec19.feature.wishlist.WishlistViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val wishlistModule = module {
    // Constructor has defaulted sample-data params; viewModelOf would try to inject them.
    viewModel { WishlistViewModel() }
}
