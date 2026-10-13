package com.example.ecommercenativec19.feature.productdetails.di

import com.example.ecommercenativec19.feature.productdetails.ProductDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val productDetailsModule = module {
    viewModel { params -> ProductDetailsViewModel(productId = params.get(), getProductDetails = get(), observeFavoriteIds = get(), toggleFavorite = get(), addToCart = get()) }
}
