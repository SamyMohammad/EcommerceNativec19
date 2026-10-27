package com.example.ecommercenativec19.feature.products.di

import com.example.ecommercenativec19.feature.products.data.ProductsApi
import com.example.ecommercenativec19.feature.products.data.ProductsRepositoryImpl
import com.example.ecommercenativec19.feature.products.domain.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsModule {
    @Binds
    @Singleton
    abstract fun bindProductsRepository(impl: ProductsRepositoryImpl): ProductsRepository

    companion object {
        @Provides
        @Singleton
        fun provideProductsApi(retrofit: Retrofit): ProductsApi = retrofit.create(ProductsApi::class.java)
    }
}
