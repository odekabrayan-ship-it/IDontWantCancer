package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents the outcome of reconciling an intelligence state with new information.
 */
enum class ReconciliationClassification {
    NO_CHANGE,
    UPDATED,
    MATERIALLY_CHANGED,
    REACTIVATED,
    REQUIRES_REVIEW,
    UNRESOLVED
}

data class IntelligenceStateReconciliationResult(
    val threadId: String,
    val classification: ReconciliationClassification,
    val previousStateId: String?,
    val resultingStateId: String,
    val triggeringContinuityLevel: ContinuityLevel,
    val triggeringTransitionId: String?,
    val reconciledAt: Instant
)
