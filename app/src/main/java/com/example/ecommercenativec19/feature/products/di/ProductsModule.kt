package com.example.ecommercenativec19.feature.products.di

import com.example.ecommercenativec19.feature.products.data.ProductsApi
import com.example.ecommercenativec19.feature.products.data.ProductsRepositoryImpl
import com.example.ecommercenativec19.feature.products.domain.GetProductDetailsUseCase
import com.example.ecommercenativec19.feature.products.domain.GetProductsUseCase
import com.example.ecommercenativec19.feature.products.domain.ProductsRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val productsModule = module {
    single<ProductsApi> { get<Retrofit>().create(ProductsApi::class.java) }
    single<ProductsRepository> { ProductsRepositoryImpl(get()) }
    factory { GetProductsUseCase(get()) }
    factory { GetProductDetailsUseCase(get()) }
}
