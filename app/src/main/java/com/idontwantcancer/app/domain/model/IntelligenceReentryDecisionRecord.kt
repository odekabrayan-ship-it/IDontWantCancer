package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * An immutable record representing a lifecycle decision candidate.
 * This is presented to the transition authority (Step 80) for final validation.
 */
@Serializable
data class IntelligenceReentryDecisionRecord(
    val reentryIdentity: String,
    val status: DecisionRecordStatus,
    val proposedState: ReentryLifecycleState?,
    val eligibilityResult: IntelligenceReentryDecisionEligibilityResult,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

/**
 * Defines the status of the decision record.
 */
@Serializable
enum class DecisionRecordStatus {
    /**
     * The decision has been presented as a valid candidate for transition.
     */
    CANDIDATE_PRESENTED,

    /**
     * The decision was blocked at the eligibility gate and is not a candidate.
     */
    INELIGIBLE_FOR_DECISION
}
