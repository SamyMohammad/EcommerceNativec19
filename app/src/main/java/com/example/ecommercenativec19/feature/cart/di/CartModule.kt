package com.example.ecommercenativec19.feature.cart.di

import com.example.ecommercenativec19.feature.cart.CartViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {
    // Constructor has defaulted sample-data params; viewModelOf would try to inject them.
    viewModel { CartViewModel() }
}
