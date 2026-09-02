package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceStateReconciliationEngine] that applies 
 * deterministic rules to establish the authoritative current state.
 */
class DefaultIntelligenceStateReconciliationEngine @Inject constructor() : IntelligenceStateReconciliationEngine {

    override fun reconcileState(
        thread: IntelligenceThread,
        previousState: ReconstructedState?,
        newState: ReconstructedState,
        continuity: IntelligenceContinuityResult,
        transition: IntelligenceStateTransition?
    ): IntelligenceStateReconciliationResult {
        val now = Instant.now()
        
        // 1. Determine Classification based on Continuity and Transition
        val classification = when (continuity.level) {
            ContinuityLevel.NO_MATERIAL_CHANGE -> ReconciliationClassification.NO_CHANGE
            ContinuityLevel.CONTINUATION -> {
                if (transition?.type == IntelligenceStateTransitionType.CONFIRMED) ReconciliationClassification.UPDATED
                else ReconciliationClassification.NO_CHANGE
            }
            ContinuityLevel.UPDATE -> ReconciliationClassification.UPDATED
            ContinuityLevel.MATERIAL_CHANGE -> ReconciliationClassification.MATERIALLY_CHANGED
            ContinuityLevel.REVERSAL -> ReconciliationClassification.MATERIALLY_CHANGED
            ContinuityLevel.REACTIVATED -> ReconciliationClassification.REACTIVATED
            ContinuityLevel.HISTORICAL_CONTEXT -> ReconciliationClassification.NO_CHANGE
            ContinuityLevel.INSUFFICIENT_CONTEXT -> ReconciliationClassification.UNRESOLVED
        }

        // 2. Conflict Check (Fallback)
        val finalClassification = if (newState.conflictStatus == ResolutionStatus.UNRESOLVED) {
            ReconciliationClassification.REQUIRES_REVIEW
        } else classification

        return IntelligenceStateReconciliationResult(
            threadId = thread.id,
            classification = finalClassification,
            previousStateId = previousState?.effectiveEntryId,
            resultingStateId = newState.effectiveEntryId,
            triggeringContinuityLevel = continuity.level,
            triggeringTransitionId = transition?.id,
            reconciledAt = now
        )
    }
}
