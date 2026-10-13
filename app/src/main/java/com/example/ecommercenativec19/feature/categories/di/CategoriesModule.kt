package com.example.ecommercenativec19.feature.categories.di

import com.example.ecommercenativec19.feature.categories.CategoriesViewModel
import com.example.ecommercenativec19.feature.categories.data.CategoriesApi
import com.example.ecommercenativec19.feature.categories.data.CategoriesRepositoryImpl
import com.example.ecommercenativec19.feature.categories.domain.CategoriesRepository
import com.example.ecommercenativec19.feature.categories.domain.GetCategoriesUseCase
import com.example.ecommercenativec19.feature.categories.domain.GetSubCategoriesUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val categoriesModule = module {
    single<CategoriesApi> { get<Retrofit>().create(CategoriesApi::class.java) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { GetSubCategoriesUseCase(get()) }
    viewModelOf(::CategoriesViewModel)
}
