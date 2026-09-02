package com.idontwantcancer.app.presentation.alerts

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract

/**
 * UI state for the Alerts screen.
 */
sealed interface AlertsUiState {
    /**
     * Alerts are being loaded.
     */
    data object Loading : AlertsUiState

    /**
     * Alerts were successfully retrieved.
     */
    data class Success(
        val signals: List<Signal>,
        /**
         * Map of signal ID to its verified re-entry reconciliation status.
         */
        val reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract> = emptyMap(),
        /**
         * Finality status of the alerts load command.
         */
        val finality: CommandConsumptionFinalityPresentationContract = CommandConsumptionFinalityPresentationContract.StatusUnavailable,
        /**
         * The ID of the currently selected signal for detail viewing.
         */
        val selectedSignalId: String? = null
    ) : AlertsUiState

    /**
     * An error occurred while retrieving alerts.
     */
    data class Error(
        val message: String
    ) : AlertsUiState
}
