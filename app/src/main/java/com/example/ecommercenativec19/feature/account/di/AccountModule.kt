package com.example.ecommercenativec19.feature.account.di

import com.example.ecommercenativec19.feature.account.AccountViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    // Constructor has defaulted sample-data params; viewModelOf would try to inject them.
    viewModel { AccountViewModel() }
}
