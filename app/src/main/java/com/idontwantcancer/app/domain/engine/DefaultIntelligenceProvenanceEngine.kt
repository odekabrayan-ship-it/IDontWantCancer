package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceProvenanceEngine] that reconstructs
 * the lineage of intelligence items using the agency's memory.
 */
class DefaultIntelligenceProvenanceEngine @Inject constructor(
    private val memory: IntelligenceMemoryRepository
) : IntelligenceProvenanceEngine {

    override suspend fun getSignalProvenance(signalId: String): IntelligenceProvenance {
        // 1. Authoritative Signal
        val signal = memory.getSignalById(signalId) 
            ?: throw IllegalArgumentException("Signal $signalId not found in memory.")

        // 2. Locate Thread and Timeline via TimelineEntry
        val threads = memory.findThreadByTopic(signal.title)?.let { listOf(it) } ?: emptyList()
        val thread = threads.firstOrNull()
        
        val timelineEntries = thread?.id?.let { memory.getTimelineEntriesForThread(it) } ?: emptyList()
        
        // 3. Identify supporting source materials
        val primarySourceMaterialId = timelineEntries.find { it.signalId == signalId }?.sourceMaterialId
        val supportingMaterialIds = signal.supportingSources.mapNotNull { it.url } 
        
        val materialIds = (listOfNotNull(primarySourceMaterialId) + supportingMaterialIds).distinct()
        val sources = materialIds.mapNotNull { memory.getSourceMaterialById(it) }

        // 4. Locate Consolidated Event
        val consolidatedEvent = memory.getAllConsolidatedEvents().find { event ->
            event.sourceMaterialIds.any { it == primarySourceMaterialId }
        }

        return IntelligenceProvenance(
            signalId = signalId,
            signal = signal,
            thread = thread,
            timelineEntries = timelineEntries,
            sources = sources,
            consolidatedEvent = consolidatedEvent,
            reconstructedAt = Instant.now()
        )
    }

    override suspend fun getBriefingProvenance(item: IntelligenceBriefingItem): IntelligenceProvenance {
        val provenance = getSignalProvenance(item.intelligenceId)
        return provenance.copy(briefingItem = item)
    }
}
