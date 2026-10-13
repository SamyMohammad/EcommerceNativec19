package com.example.ecommercenativec19.feature.account.di

import com.example.ecommercenativec19.feature.account.AccountViewModel
import com.example.ecommercenativec19.feature.account.data.AccountApi
import com.example.ecommercenativec19.feature.account.data.AccountRepositoryImpl
import com.example.ecommercenativec19.feature.account.domain.AccountRepository
import com.example.ecommercenativec19.feature.account.domain.ChangePasswordUseCase
import com.example.ecommercenativec19.feature.account.domain.GetProfileUseCase
import com.example.ecommercenativec19.feature.account.domain.UpdateProfileUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val accountModule = module {
    single<AccountApi> { get<Retrofit>().create(AccountApi::class.java) }
    single<AccountRepository> { AccountRepositoryImpl(get(), get(), get()) }
    factory { GetProfileUseCase(get()) }
    factory { UpdateProfileUseCase(get()) }
    factory { ChangePasswordUseCase(get()) }
    viewModelOf(::AccountViewModel)
}
