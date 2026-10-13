package com.example.ecommercenativec19.feature.signin.di

import com.example.ecommercenativec19.feature.signin.SignInViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signInModule = module {
    viewModelOf(::SignInViewModel)
}
