package com.example.ecommercenativec19.feature.signup.di

import com.example.ecommercenativec19.feature.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signUpModule = module {
    viewModelOf(::SignUpViewModel)
}
