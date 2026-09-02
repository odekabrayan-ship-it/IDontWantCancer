package com.idontwantcancer.app.presentation.home

import com.idontwantcancer.app.domain.model.IntelligenceBriefing
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
        /**
         * Map of signal ID to its verified re-entry reconciliation status.
         */
        val reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract> = emptyMap(),
        /**
         * Finality status of the briefing load command.
         */
        val finality: CommandConsumptionFinalityPresentationContract = CommandConsumptionFinalityPresentationContract.StatusUnavailable
    ) : HomeUiState

    /**
     * An error occurred while retrieving the briefing.
     */
    data class Error(
        val message: String
    ) : HomeUiState
}
