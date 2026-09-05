package com.idontwantcancer.app.presentation.model

/**
 * Defines the authoritative UI interactions originating from the presentation layer.
 * These are translated into application commands or navigation actions.
 */
sealed interface IntelligenceUiInteraction {
    /**
     * User requested to view the details of a specific signal.
     */
    data class ViewSignalDetails(val signalId: String) : IntelligenceUiInteraction

    /**
     * User requested to navigate back to the previous screen.
     */
    data object NavigateBack : IntelligenceUiInteraction

    /**
     * User requested to retry a failed operation (e.g. briefing load).
     */
    data object RetryOperation : IntelligenceUiInteraction

    /**
     * User requested an intelligence search.
     */
    data class PerformSearch(val query: String) : IntelligenceUiInteraction

    /**
     * User requested to clear the current search query.
     */
    data object ClearSearch : IntelligenceUiInteraction

    /**
     * User requested to cancel an in-progress operation.
     */
    data class CancelOperation(val targetInteraction: IntelligenceUiInteraction) : IntelligenceUiInteraction

    /**
     * User requested to clear the current selection (e.g. closing a detail pane).
     */
    data object ClearSelection : IntelligenceUiInteraction

    /**
     * User acknowledged or completed the directive associated with a signal.
     */
    data class AcknowledgeSignal(val signalId: String) : IntelligenceUiInteraction
}
