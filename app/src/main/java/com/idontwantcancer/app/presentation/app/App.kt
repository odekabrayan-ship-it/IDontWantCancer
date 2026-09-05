package com.idontwantcancer.app.presentation.app

import androidx.compose.runtime.Composable
import com.idontwantcancer.app.core.ui.theme.IDontWantCancerTheme
import com.idontwantcancer.app.presentation.navigation.AppNavigation

@Composable
fun App(initialSignalId: String? = null) {
    IDontWantCancerTheme {
        AppNavigation(initialSignalId = initialSignalId)
    }
}
