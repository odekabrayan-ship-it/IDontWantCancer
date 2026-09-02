package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceStateReconstructionEngine] that uses 
 * deterministic chronological rules and supersession relationships to derive 
 * current understanding.
 */
class DefaultIntelligenceStateReconstructionEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceStateReconstructionEngine {

    override suspend fun reconstructCurrentState(timeline: IntelligenceEventTimeline): ReconstructedState {
        val sorted = timeline.sortedEntries
        if (sorted.isEmpty()) {
            throw IllegalArgumentException("Cannot reconstruct state for an empty timeline.")
        }

        // 1. Collect all supersession relations for this timeline
        val supersededIds = mutableSetOf<String>()
        for (entry in sorted) {
            val relations = memory.getSupersessionRelationsForEntry(entry.id)
            relations.forEach { 
                if (it.supersedingEntryId != entry.id) {
                    // This entry is the *previous* entry in a relation
                    supersededIds.add(it.previousEntryId)
                }
            }
        }

        // 2. Identify the effective entry
        // We prioritize terminal states like RESOLUTION or REVERSAL if they are not themselves superseded.
        val lastResolution = sorted.lastOrNull { it.type == TimelineEntryType.RESOLUTION && !supersededIds.contains(it.id) }
        val lastReversal = sorted.lastOrNull { it.type == TimelineEntryType.REVERSAL && !supersededIds.contains(it.id) }
        
        val validEntries = sorted.filter { !supersededIds.contains(it.id) }

        val primaryEntry = lastResolution ?: lastReversal ?: validEntries.lastOrNull() ?: sorted.last()

        // 3. Fetch associated Signal context if available
        val signal = primaryEntry.signalId?.let { memory.getSignalById(it) }

        // 4. Identify last meaningful change
        val lastChangeEntry = sorted.lastOrNull { 
            it.type == TimelineEntryType.MATERIAL_CHANGE || 
            it.type == TimelineEntryType.RECOMMENDATION_CHANGE ||
            it.type == TimelineEntryType.REGULATORY_ACTION ||
            it.type == TimelineEntryType.REVERSAL
        }

        return ReconstructedState(
            threadId = timeline.threadId,
            summary = primaryEntry.description,
            effectiveEntryId = primaryEntry.id,
            confidence = signal?.confidence,
            conflictStatus = signal?.conflictStatus,
            lastMeaningfulChangeId = lastChangeEntry?.changeId,
            reconstructedAt = Instant.now()
        )
    }
}
