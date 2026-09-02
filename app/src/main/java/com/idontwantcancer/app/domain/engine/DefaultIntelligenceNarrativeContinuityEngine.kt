package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceNarrativeContinuityEngine] that derives
 * a structured narrative from historical intelligence states and transitions.
 */
class DefaultIntelligenceNarrativeContinuityEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceNarrativeContinuityEngine {

    override suspend fun reconstructNarrative(thread: IntelligenceThread): IntelligenceNarrativeSequence {
        val entries = memory.getTimelineEntriesForThread(thread.id)
        val transitions = memory.getStateTransitionsForThread(thread.id)
        
        val narrativeEvents = mutableListOf<IntelligenceNarrativeEvent>()

        for (entry in entries) {
            val transition = transitions.find { it.triggeringEventId == entry.id }
            val relations = memory.getSupersessionRelationsForEntry(entry.id)
            val conflict = memory.getConflictsForTopic(thread.topicIdentifier).find { it.competingSignalIds.contains(entry.signalId) }

            narrativeEvents.add(
                IntelligenceNarrativeEvent(
                    id = "narrative-${entry.id}",
                    threadId = thread.id,
                    category = mapToNarrativeCategory(entry, transition),
                    description = entry.description,
                    effectiveTime = entry.eventTime ?: entry.sourceTime ?: entry.publicationTime ?: entry.ingestionTime,
                    timelineEntryId = entry.id,
                    signalId = entry.signalId,
                    transitionId = transition?.id,
                    conflictId = conflict?.id,
                    supersessionRelationId = relations.firstOrNull()?.id, // Simplification: first relation
                    isCurrentState = entry.description == thread.currentStatus
                )
            )
        }

        return IntelligenceNarrativeSequence(
            threadId = thread.id,
            topicIdentifier = thread.topicIdentifier,
            events = narrativeEvents
        )
    }

    private fun mapToNarrativeCategory(entry: TimelineEntry, transition: IntelligenceStateTransition?): IntelligenceNarrativeEventCategory {
        return when (transition?.type) {
            IntelligenceStateTransitionType.INITIALIZED -> IntelligenceNarrativeEventCategory.INITIAL_DETECTION
            IntelligenceStateTransitionType.REPLACED -> IntelligenceNarrativeEventCategory.SUPERSESSION
            IntelligenceStateTransitionType.CORRECTED -> IntelligenceNarrativeEventCategory.MATERIAL_CHANGE
            IntelligenceStateTransitionType.CONTESTED -> IntelligenceNarrativeEventCategory.CONFLICT
            IntelligenceStateTransitionType.RESOLVED -> IntelligenceNarrativeEventCategory.RESOLUTION
            else -> when (entry.type) {
                TimelineEntryType.REVERSAL -> IntelligenceNarrativeEventCategory.REVERSAL
                TimelineEntryType.RECOMMENDATION_CHANGE -> IntelligenceNarrativeEventCategory.MATERIAL_CHANGE
                TimelineEntryType.MATERIAL_CHANGE -> IntelligenceNarrativeEventCategory.MATERIAL_CHANGE
                TimelineEntryType.OFFICIAL_CONFIRMATION -> IntelligenceNarrativeEventCategory.UPDATE
                TimelineEntryType.EVIDENCE_UPGRADE -> IntelligenceNarrativeEventCategory.UPDATE
                else -> IntelligenceNarrativeEventCategory.CONTINUATION
            }
        }
    }
}
