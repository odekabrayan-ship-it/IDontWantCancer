package com.idontwantcancer.app.presentation.home

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
import com.idontwantcancer.app.domain.model.PreventionAction
import com.idontwantcancer.app.domain.model.UserMission
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract

/**
 * UI state for the Home screen briefing.
 */
sealed interface HomeUiState {
    /**
     * The briefing is being loaded.
     */
    data object Loading : HomeUiState

    /**
     * The briefing was successfully retrieved.
     */
    data class Success(
        val briefing: IntelligenceBriefing,
        val userCountry: String,
        val userMission: UserMission = UserMission.PREVENTION,
        val adoptedActions: List<PreventionAction> = emptyList(),
        /**
         * Map of signal ID to its verified re-entry reconciliation status.
         */
        val reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract> = emptyMap(),
        /**
         * Contract representing the finality/sync status of the current briefing.
         */
        val finality: CommandConsumptionFinalityPresentationContract = CommandConsumptionFinalityPresentationContract.NonTerminal(
            operationId = "initial_load",
            detail = "Initializing agency briefing..."
        )
    ) : HomeUiState

    /**
     * An error occurred while retrieving the briefing.
     */
    data class Error(val message: String) : HomeUiState
}
