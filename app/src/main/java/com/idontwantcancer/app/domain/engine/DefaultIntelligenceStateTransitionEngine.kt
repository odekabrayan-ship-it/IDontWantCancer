package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceStateTransitionEngine] that applies
 * deterministic rules to transform the agency's understanding.
 */
class DefaultIntelligenceStateTransitionEngine @Inject constructor() : IntelligenceStateTransitionEngine {

    override fun evaluateTransition(
        previousState: ReconstructedState?,
        newEntry: TimelineEntry,
        relations: List<SupersessionRelation>,
        conflicts: List<IntelligenceConflict>,
        synthesis: EvidenceSynthesisResult
    ): IntelligenceStateTransition? {
        val effectiveAt = newEntry.eventTime ?: newEntry.sourceTime ?: newEntry.publicationTime ?: newEntry.ingestionTime

        // 1. Initial State
        if (previousState == null) {
            return if (newEntry.type == TimelineEntryType.INITIAL_OBSERVATION || newEntry.type == TimelineEntryType.MATERIAL_CHANGE) {
                IntelligenceStateTransition(
                    id = UUID.randomUUID().toString(),
                    threadId = newEntry.threadId,
                    previousStateEntryId = null,
                    resultingStateEntryId = newEntry.id,
                    type = IntelligenceStateTransitionType.INITIALIZED,
                    triggeringEventId = newEntry.id,
                    effectiveAt = effectiveAt,
                    reason = "Initial intelligence state established with synthesis level: ${synthesis.level}"
                )
            } else null
        }

        // 2. Conflict / Contested State (Integration with Synthesis)
        if (synthesis.level == EvidenceSynthesisLevel.CONTESTED && previousState.conflictStatus != ResolutionStatus.UNRESOLVED) {
            return IntelligenceStateTransition(
                id = UUID.randomUUID().toString(),
                threadId = newEntry.threadId,
                previousStateEntryId = previousState.effectiveEntryId,
                resultingStateEntryId = newEntry.id,
                type = IntelligenceStateTransitionType.CONTESTED,
                triggeringEventId = newEntry.id,
                effectiveAt = effectiveAt,
                reason = "Intelligence state is now contested due to unresolved contradictions."
            )
        }

        // 3. Supersession-based Transitions
        val primaryRelation = relations.find { it.supersedingEntryId == newEntry.id }
        if (primaryRelation != null) {
            val type = when (primaryRelation.type) {
                SupersessionType.CORRECTION -> IntelligenceStateTransitionType.CORRECTED
                SupersessionType.RETRACTION -> IntelligenceStateTransitionType.RETRACTED
                SupersessionType.REPLACEMENT -> IntelligenceStateTransitionType.REPLACED
                SupersessionType.OFFICIAL_UPDATE -> IntelligenceStateTransitionType.UPDATED
                SupersessionType.STATUS_CHANGE -> IntelligenceStateTransitionType.UPDATED
            }

            return IntelligenceStateTransition(
                id = UUID.randomUUID().toString(),
                threadId = newEntry.threadId,
                previousStateEntryId = previousState.effectiveEntryId,
                resultingStateEntryId = newEntry.id,
                type = type,
                triggeringEventId = newEntry.id,
                effectiveAt = effectiveAt,
                reason = "State transitioned via ${primaryRelation.type}: ${primaryRelation.reason}"
            )
        }

        // 4. Lifecycle Transitions (Resolution / Reactivation)
        if (newEntry.type == TimelineEntryType.RESOLUTION) {
            return IntelligenceStateTransition(
                id = UUID.randomUUID().toString(),
                threadId = newEntry.threadId,
                previousStateEntryId = previousState.effectiveEntryId,
                resultingStateEntryId = newEntry.id,
                type = IntelligenceStateTransitionType.RESOLVED,
                triggeringEventId = newEntry.id,
                effectiveAt = effectiveAt,
                reason = "Intelligence topic formally resolved."
            )
        }

        // 5. Corroboration / Confirmation
        if (synthesis.level == EvidenceSynthesisLevel.STRONGLY_SUPPORTED && previousState.confidence != SignalConfidence.VERY_HIGH) {
            return IntelligenceStateTransition(
                id = UUID.randomUUID().toString(),
                threadId = newEntry.threadId,
                previousStateEntryId = previousState.effectiveEntryId,
                resultingStateEntryId = newEntry.id,
                type = IntelligenceStateTransitionType.CONFIRMED,
                triggeringEventId = newEntry.id,
                effectiveAt = effectiveAt,
                reason = "Current understanding strongly supported by independent corroboration."
            )
        }

        return null
    }
}
