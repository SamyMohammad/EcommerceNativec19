package com.example.ecommercenativec19.di

import android.content.Context
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsViewModel
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

class KoinModulesTest {
    // Verifies the exact list EcommerceApp starts Koin with, so a missing binding fails here, not at first screen open.
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `production module graph resolves every definition`() {
        module { includes(appModules) }.verify(
            // Provided at runtime by androidContext(); not a definition Koin can see statically.
            extraTypes = listOf(Context::class),
            injections = injectedParameters(definition<ProductDetailsViewModel>(String::class)),
        )
    }
}
