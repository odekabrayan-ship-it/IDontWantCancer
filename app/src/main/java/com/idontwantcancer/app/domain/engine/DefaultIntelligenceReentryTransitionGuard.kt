package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the transition guard that enforces the re-entry
 * lifecycle state machine rules.
 */
class DefaultIntelligenceReentryTransitionGuard @Inject constructor() : IntelligenceReentryTransitionGuard {

    override fun validateTransition(
        currentLifecycle: IntelligenceReentryLifecycle,
        targetState: ReentryLifecycleState
    ): ReentryTransitionResult {
        val currentState = currentLifecycle.currentState

        // 1. Idempotency Check: Transition to same state is always valid
        if (currentState == targetState) {
            return ReentryTransitionResult.Accepted(currentLifecycle)
        }

        // 2. Terminal State Check
        if (isTerminal(currentState)) {
            return ReentryTransitionResult.Rejected(
                reason = ReentryTransitionRejectionReason.TERMINAL_STATE,
                currentState = currentState,
                requestedState = targetState
            )
        }

        // 3. Define Valid Transitions
        val isValid = when (currentState) {
            ReentryLifecycleState.ADMITTED -> {
                targetState == ReentryLifecycleState.PROCESSING || 
                targetState == ReentryLifecycleState.SUPERSEDED
            }
            ReentryLifecycleState.PROCESSING -> {
                targetState == ReentryLifecycleState.COMPLETED || 
                targetState == ReentryLifecycleState.REJECTED || 
                targetState == ReentryLifecycleState.SUPERSEDED
            }
            else -> false // Terminal states already handled
        }

        return if (isValid) {
            ReentryTransitionResult.Accepted(
                currentLifecycle.copy(
                    currentState = targetState,
                    lastTransitionAt = Instant.now()
                )
            )
        } else {
            ReentryTransitionResult.Rejected(
                reason = ReentryTransitionRejectionReason.INVALID_TRANSITION,
                currentState = currentState,
                requestedState = targetState
            )
        }
    }

    override fun validateDecision(
        currentLifecycle: IntelligenceReentryLifecycle,
        record: IntelligenceReentryDecisionRecord
    ): ReentryTransitionResult {
        // Enforce that the decision record must be a valid candidate (Step 95/96 logic)
        if (record.status != DecisionRecordStatus.CANDIDATE_PRESENTED || record.proposedState == null) {
            return ReentryTransitionResult.Rejected(
                reason = ReentryTransitionRejectionReason.INVALID_TRANSITION,
                currentState = currentLifecycle.currentState,
                requestedState = currentLifecycle.currentState // No change proposed
            )
        }

        // Delegate to the standard transition validation rules
        return validateTransition(currentLifecycle, record.proposedState)
    }

    private fun isTerminal(state: ReentryLifecycleState): Boolean {
        return state == ReentryLifecycleState.COMPLETED || 
               state == ReentryLifecycleState.REJECTED || 
               state == ReentryLifecycleState.SUPERSEDED
    }
}
