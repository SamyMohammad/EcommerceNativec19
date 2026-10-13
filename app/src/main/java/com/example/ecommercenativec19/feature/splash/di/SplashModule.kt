package com.example.ecommercenativec19.feature.splash.di

import com.example.ecommercenativec19.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val splashModule = module {
    viewModelOf(::SplashViewModel)
}
