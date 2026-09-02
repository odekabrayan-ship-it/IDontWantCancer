package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for maintaining the 
 * authoritative lifecycle state of re-entry events.
 */
interface IntelligenceReentryLifecycleLedger {
    /**
     * Initializes a new lifecycle record for an admitted re-entry event.
     *
     * @param deduplicationResult The result of the deduplication gate.
     * @return The initial lifecycle record.
     */
    suspend fun admitEvent(
        deduplicationResult: IntelligenceReentryDeduplicationResult
    ): IntelligenceReentryLifecycle

    /**
     * Transitions a re-entry event to a new lifecycle state.
     *
     * @param identity The unique re-entry identity.
     * @param newState The target lifecycle state.
     * @param reason The reason for the transition.
     * @return The structured transition result (Accepted or Rejected).
     */
    suspend fun transitionState(
        identity: String,
        newState: ReentryLifecycleState,
        reason: String? = null
    ): ReentryTransitionResult

    /**
     * Transitions a re-entry event based on a formal decision record.
     *
     * @param record The formal decision record from Step 96.
     * @return The structured transition result.
     */
    suspend fun transitionByDecision(
        record: IntelligenceReentryDecisionRecord
    ): ReentryTransitionResult
}
