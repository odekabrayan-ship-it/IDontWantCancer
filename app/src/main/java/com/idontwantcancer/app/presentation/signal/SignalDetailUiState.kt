package com.idontwantcancer.app.presentation.signal

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract

/**
 * UI state for the Signal Detail screen.
 */
sealed interface SignalDetailUiState {
    /**
     * Signal details are being loaded.
     */
    data object Loading : SignalDetailUiState

    /**
     * Signal details were successfully retrieved.
     */
    data class Success(
        val signal: Signal,
        /**
         * The verified re-entry reconciliation status for this signal.
         */
        val reconciliation: IntelligenceReentryReconciliationPresentationContract =
            IntelligenceReentryReconciliationPresentationContract.StatusUnavailable,
        /**
         * Finality status of the detail load command.
         */
        val finality: CommandConsumptionFinalityPresentationContract = CommandConsumptionFinalityPresentationContract.StatusUnavailable
    ) : SignalDetailUiState

    /**
     * The requested signal was not found.
     */
    data object NotFound : SignalDetailUiState

    /**
     * An error occurred while retrieving signal details.
     */
    data class Error(
        val message: String
    ) : SignalDetailUiState
}
