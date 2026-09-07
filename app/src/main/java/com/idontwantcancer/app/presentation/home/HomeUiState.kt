package com.idontwantcancer.app.presentation.home

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.model.PreventionAction
import com.idontwantcancer.app.domain.model.UserMission
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract

/**
 * UI state for the Home screen briefing dashboard.
 */
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val briefing: IntelligenceBriefing,
        val userCountry: String,
        val userMission: UserMission = UserMission.PREVENTION,
        val adoptedActions: List<PreventionAction> = emptyList(),
        val dailyPeace: DailyPeace? = null,
        val pillarStatuses: List<PillarStatus> = emptyList(),
        val reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract> = emptyMap(),
        val finality: CommandConsumptionFinalityPresentationContract = CommandConsumptionFinalityPresentationContract.NonTerminal(
            operationId = "initial_load",
            detail = "Initializing agency briefing..."
        )
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

data class DailyPeace(
    val title: String,
    val summary: String,
    val shield: String
)

data class PillarStatus(
    val id: String,
    val title: String,
    val status: String,
    val isAlert: Boolean = false,
    val progress: Float? = null
)
