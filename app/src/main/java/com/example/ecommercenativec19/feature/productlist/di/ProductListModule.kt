package com.example.ecommercenativec19.feature.productlist.di

import com.example.ecommercenativec19.feature.productlist.ProductListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val productListModule = module {
    // Constructor has defaulted sample-data params; viewModelOf would try to inject them.
    viewModel { ProductListViewModel() }
}
