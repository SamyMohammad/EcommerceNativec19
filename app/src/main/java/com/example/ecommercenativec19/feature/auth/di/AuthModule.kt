package com.example.ecommercenativec19.feature.auth.di

import com.example.ecommercenativec19.feature.auth.data.AuthApi
import com.example.ecommercenativec19.feature.auth.data.AuthRepositoryImpl
import com.example.ecommercenativec19.feature.auth.domain.AuthRepository
import com.example.ecommercenativec19.feature.auth.domain.ObserveSessionUseCase
import com.example.ecommercenativec19.feature.auth.domain.SignInUseCase
import com.example.ecommercenativec19.feature.auth.domain.SignOutUseCase
import com.example.ecommercenativec19.feature.auth.domain.SignUpUseCase
import org.koin.dsl.module
import retrofit2.Retrofit

val authModule = module {
    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { ObserveSessionUseCase(get()) }
}
