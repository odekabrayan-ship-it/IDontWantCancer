package com.idontwantcancer.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
) {
    data object Home : NavigationItem(Screen.Home, "Home", Icons.Default.Home)
    data object Alerts : NavigationItem(Screen.Alerts, "Alerts", Icons.Default.Notifications)
    data object Search : NavigationItem(Screen.Search, "Search", Icons.Default.Search)
    data object Settings : NavigationItem(Screen.Settings, "Settings", Icons.Default.Settings)
}
