package com.example.ecommercenativec19.feature.productlist.di

import com.example.ecommercenativec19.feature.productlist.ProductListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val productListModule = module {
    // The sub-category id comes from the navigation key as a Koin parameter.
    viewModel { params -> ProductListViewModel(subCategoryId = params.get(), getProducts = get(), observeFavoriteIds = get(), toggleFavorite = get()) }
}
