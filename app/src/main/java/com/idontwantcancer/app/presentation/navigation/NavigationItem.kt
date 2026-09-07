package com.idontwantcancer.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.ui.graphics.vector.ImageVector
import com.idontwantcancer.app.R

sealed class NavigationItem(
    val screen: Screen,
    val labelRes: Int,
    val icon: ImageVector
) {
    // Prevention Mission Tabs
    data object Home : NavigationItem(Screen.Home, R.string.nav_home, Icons.Default.Home)
    data object Verify : NavigationItem(Screen.Search(), R.string.nav_inquiry, Icons.Default.HealthAndSafety)
    data object Settings : NavigationItem(Screen.Settings, R.string.nav_settings, Icons.Default.Settings)
    
    // Healing Mission Tabs
    data object HealingSanctuary : NavigationItem(Screen.HealingSanctuary, R.string.sanctuary_title, Icons.Default.Healing)
    data object MyJourney : NavigationItem(Screen.MyJourney, R.string.sanctuary_section_progress, Icons.Default.History)

    // Sub-Hubs (Deep-linked from Dashboard)
    data object Alerts : NavigationItem(Screen.Alerts, R.string.nav_alerts, Icons.Default.Notifications)
    data object Prevention : NavigationItem(Screen.Prevention, R.string.nav_prevention, Icons.Default.Verified)
}
