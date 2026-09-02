package com.idontwantcancer.app.presentation.search

import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceReentryReconciliationPresentationContract

/**
 * UI state for the Search screen.
 */
sealed interface SearchUiState {
    /**
     * Initial idle state.
     */
    data object Idle : SearchUiState

    /**
     * Searching in progress.
     */
    data object Searching : SearchUiState

    /**
     * No results found.
     */
    data object Empty : SearchUiState

    /**
     * Search results successfully retrieved.
     */
    data class Success(
        val signals: List<Signal>,
        /**
         * Map of signal ID to its verified re-entry reconciliation status.
         */
        val reconciliations: Map<String, IntelligenceReentryReconciliationPresentationContract> = emptyMap(),
        /**
         * Finality status of the search command.
         */
        val finality: CommandConsumptionFinalityPresentationContract = CommandConsumptionFinalityPresentationContract.StatusUnavailable,
        /**
         * The ID of the currently selected signal for detail viewing.
         */
        val selectedSignalId: String? = null
    ) : SearchUiState

    /**
     * An error occurred while searching.
     */
    data class Error(
        val message: String
    ) : SearchUiState
}
