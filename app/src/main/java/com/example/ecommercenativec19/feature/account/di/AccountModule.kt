package com.example.ecommercenativec19.feature.account.di

import com.example.ecommercenativec19.feature.account.data.AccountApi
import com.example.ecommercenativec19.feature.account.data.AccountRepositoryImpl
import com.example.ecommercenativec19.feature.account.domain.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountModule {
    @Binds
    @Singleton
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    companion object {
        @Provides
        @Singleton
        fun provideAccountApi(retrofit: Retrofit): AccountApi = retrofit.create(AccountApi::class.java)
    }
}
