package com.example.ecommercenativec19.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.core.ui.AppTab
import com.example.ecommercenativec19.core.ui.BottomNavigationBar

/**
 * The one shared bottom bar. A null [selectedTab] hides it. Only the bottom inset reaches [content]: every
 * screen draws its own header under the status bar.
 */
@Composable
fun AppScaffold(
    selectedTab: AppTab?,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (selectedTab != null) BottomNavigationBar(selectedTab = selectedTab, onTabSelected = onTabSelected)
        },
    ) { innerPadding ->
        Box(Modifier.padding(bottom = innerPadding.calculateBottomPadding())) { content() }
    }
}
