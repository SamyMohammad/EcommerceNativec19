package com.example.ecommercenativec19.feature.home.di

import com.example.ecommercenativec19.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
}
