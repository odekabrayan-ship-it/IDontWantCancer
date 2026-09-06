package com.idontwantcancer.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Alerts : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Prevention : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object HealingSanctuary : Screen

    @Serializable
    data object MyJourney : Screen

    @Serializable
    data class SignalDetail(val signalId: String) : Screen
}
