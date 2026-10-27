package com.example.ecommercenativec19.feature.categories.di

import com.example.ecommercenativec19.feature.categories.data.CategoriesApi
import com.example.ecommercenativec19.feature.categories.data.CategoriesRepositoryImpl
import com.example.ecommercenativec19.feature.categories.domain.CategoriesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoriesModule {
    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(impl: CategoriesRepositoryImpl): CategoriesRepository

    companion object {
        @Provides
        @Singleton
        fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi = retrofit.create(CategoriesApi::class.java)
    }
}
