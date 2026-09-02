package com.idontwantcancer.app.presentation.model

/**
 * A safe, immutable presentation contract for reconciled application state.
 * Refines the Step 107 application read model for safe UI consumption.
 */
sealed interface IntelligenceReentryReconciliationPresentationContract {
    /**
     * The unique identity of the re-entry event.
     */
    val reentryIdentity: String

    /**
     * The application state is perfectly consistent with the scientific update.
     */
    data class VerifiedConsistent(
        override val reentryIdentity: String
    ) : IntelligenceReentryReconciliationPresentationContract

    /**
     * A discrepancy exists between the application state and the authoritative update.
     */
    data class InconsistentMismatch(
        override val reentryIdentity: String,
        val details: String?
    ) : IntelligenceReentryReconciliationPresentationContract

    /**
     * Reconciliation status is currently unknown or unavailable.
     */
    data object StatusUnavailable : IntelligenceReentryReconciliationPresentationContract {
        override val reentryIdentity: String = ""
    }
}
