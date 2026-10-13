package com.example.ecommercenativec19

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecommercenativec19.navigation.AppNavHost
import com.example.ecommercenativec19.ui.theme.generated.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // The design is light-only; Material dynamic color stays off (generated AppTheme has none).
            AppTheme(dark = false) {
                AppNavHost()
            }
        }
    }
}
