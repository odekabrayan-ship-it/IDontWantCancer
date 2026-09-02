package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryDecisionRecord
import com.idontwantcancer.app.domain.model.IntelligenceReentryLifecycle
import com.idontwantcancer.app.domain.model.ReentryLifecycleState
import com.idontwantcancer.app.domain.model.ReentryTransitionResult

/**
 * Interface for the intelligence component responsible for enforcing 
 * valid lifecycle transitions for re-entry events.
 */
interface IntelligenceReentryTransitionGuard {
    /**
     * Validates a requested transition for a re-entry event.
     *
     * @param currentLifecycle The current lifecycle record.
     * @param targetState The requested next state.
     * @return The structured transition result (Accepted or Rejected).
     */
    fun validateTransition(
        currentLifecycle: IntelligenceReentryLifecycle,
        targetState: ReentryLifecycleState
    ): ReentryTransitionResult

    /**
     * Validates a formal decision record for a re-entry event.
     *
     * @param currentLifecycle The current lifecycle record.
     * @param record The formal decision record from Step 96.
     * @return The structured transition result.
     */
    fun validateDecision(
        currentLifecycle: IntelligenceReentryLifecycle,
        record: IntelligenceReentryDecisionRecord
    ): ReentryTransitionResult
}
