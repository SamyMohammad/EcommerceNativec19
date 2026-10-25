package com.example.ecommercenativec19

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecommercenativec19.feature.auth.domain.ObserveSessionUseCase
import com.example.ecommercenativec19.navigation.AppNavDisplay
import com.example.ecommercenativec19.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var observeSession: ObserveSessionUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // The design is light-only; Material dynamic color stays off (AppTheme has none).
            AppTheme {
                AppNavDisplay(observeSession = observeSession)
            }
        }
    }
}
