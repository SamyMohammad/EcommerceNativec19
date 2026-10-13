package com.example.ecommercenativec19.di

import com.example.ecommercenativec19.core.network.networkModule
import com.example.ecommercenativec19.core.session.sessionModule
import com.example.ecommercenativec19.feature.account.di.accountModule
import com.example.ecommercenativec19.feature.auth.di.authModule
import com.example.ecommercenativec19.feature.cart.di.cartModule
import com.example.ecommercenativec19.feature.categories.di.categoriesModule
import com.example.ecommercenativec19.feature.home.di.homeModule
import com.example.ecommercenativec19.feature.productdetails.di.productDetailsModule
import com.example.ecommercenativec19.feature.productlist.di.productListModule
import com.example.ecommercenativec19.feature.products.di.productsModule
import com.example.ecommercenativec19.feature.signin.di.signInModule
import com.example.ecommercenativec19.feature.signup.di.signUpModule
import com.example.ecommercenativec19.feature.splash.di.splashModule
import com.example.ecommercenativec19.feature.wishlist.di.wishlistModule

// Single source of truth for the production graph; KoinModulesTest verifies this same list.
val appModules = listOf(
    sessionModule,
    networkModule,
    authModule,
    categoriesModule,
    productsModule,
    homeModule,
    productListModule,
    productDetailsModule,
    cartModule,
    wishlistModule,
    accountModule,
    signInModule,
    signUpModule,
    splashModule,
)
