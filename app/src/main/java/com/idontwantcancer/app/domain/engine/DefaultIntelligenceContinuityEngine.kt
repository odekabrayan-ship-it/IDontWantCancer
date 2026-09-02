package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceContinuityEngine] that uses 
 * deterministic rules to classify longitudinal intelligence relationships.
 */
class DefaultIntelligenceContinuityEngine @Inject constructor() : IntelligenceContinuityEngine {

    override fun evaluateContinuity(
        newEntry: TimelineEntry,
        signal: Signal?,
        previousState: ReconstructedState?,
        thread: IntelligenceThread,
        relations: List<SupersessionRelation>
    ): IntelligenceContinuityResult {
        val now = Instant.now()
        
        // 1. Handle No Previous State
        if (previousState == null) {
            return IntelligenceContinuityResult(
                level = ContinuityLevel.CONTINUATION,
                threadId = thread.id,
                signalId = signal?.id,
                previousStateEntryId = null,
                reason = "Initial observation established for the topic.",
                evaluatedAt = now
            )
        }

        // 2. Identify Reversal
        if (newEntry.type == TimelineEntryType.REVERSAL) {
            return IntelligenceContinuityResult(
                level = ContinuityLevel.REVERSAL,
                threadId = thread.id,
                signalId = signal?.id,
                previousStateEntryId = previousState.effectiveEntryId,
                reason = "New intelligence materially reverses the previously established state.",
                evaluatedAt = now
            )
        }

        // 3. Identify Reactivation
        val isThreadResolved = thread.currentStatus?.contains("resolved", ignoreCase = true) == true
        if (isThreadResolved && (newEntry.type == TimelineEntryType.MATERIAL_CHANGE || newEntry.type == TimelineEntryType.REGULATORY_ACTION)) {
            return IntelligenceContinuityResult(
                level = ContinuityLevel.REACTIVATED,
                threadId = thread.id,
                signalId = signal?.id,
                previousStateEntryId = previousState.effectiveEntryId,
                reason = "Resolved topic reactivated by meaningful new development.",
                evaluatedAt = now
            )
        }

        // 4. Identify Material Change (via Supersession or Type)
        val hasAuthoritativeSupersession = relations.any { 
            it.supersedingEntryId == newEntry.id && it.type == SupersessionType.REPLACEMENT 
        }
        if (hasAuthoritativeSupersession || newEntry.type == TimelineEntryType.RECOMMENDATION_CHANGE || newEntry.type == TimelineEntryType.REGULATORY_ACTION) {
            return IntelligenceContinuityResult(
                level = ContinuityLevel.MATERIAL_CHANGE,
                threadId = thread.id,
                signalId = signal?.id,
                previousStateEntryId = previousState.effectiveEntryId,
                reason = "Intelligence represents a material change in official status or recommendations.",
                evaluatedAt = now
            )
        }

        // 5. Identify Updates & Corroborations
        if (newEntry.type == TimelineEntryType.EVIDENCE_UPGRADE || newEntry.type == TimelineEntryType.OFFICIAL_CONFIRMATION) {
             return IntelligenceContinuityResult(
                level = ContinuityLevel.UPDATE,
                threadId = thread.id,
                signalId = signal?.id,
                previousStateEntryId = previousState.effectiveEntryId,
                reason = "Intelligence adds corroborating evidence to the existing state.",
                evaluatedAt = now
            )
        }

        // 6. Handle Corrections
        if (newEntry.type == TimelineEntryType.CORRECTION) {
            return IntelligenceContinuityResult(
                level = ContinuityLevel.MATERIAL_CHANGE, // Corrections are material changes to the state
                threadId = thread.id,
                signalId = signal?.id,
                previousStateEntryId = previousState.effectiveEntryId,
                reason = "Established state corrected by authoritative source.",
                evaluatedAt = now
            )
        }

        // 7. Default to Continuation
        return IntelligenceContinuityResult(
            level = ContinuityLevel.CONTINUATION,
            threadId = thread.id,
            signalId = signal?.id,
            previousStateEntryId = previousState.effectiveEntryId,
            reason = "Intelligence is a continuation of the existing topic development.",
            evaluatedAt = now
        )
    }
}
