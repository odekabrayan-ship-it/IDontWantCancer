package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import java.util.*
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceSupersessionEngine] that uses 
 * semantic rules to link superseding intelligence events.
 */
class DefaultIntelligenceSupersessionEngine @Inject constructor() : IntelligenceSupersessionEngine {

    override fun detectSupersession(
        newEntry: TimelineEntry,
        existingEntries: List<TimelineEntry>
    ): List<SupersessionRelation> {
        val relations = mutableListOf<SupersessionRelation>()
        val now = Instant.now()

        when (newEntry.type) {
            TimelineEntryType.CORRECTION -> {
                // A correction supersedes the most recent material change or observation
                val target = existingEntries.findLast { 
                    it.type == TimelineEntryType.MATERIAL_CHANGE || 
                    it.type == TimelineEntryType.INITIAL_OBSERVATION 
                }
                if (target != null) {
                    relations.add(createRelation(target.id, newEntry.id, SupersessionType.CORRECTION, "Formal correction issued", now))
                }
            }
            TimelineEntryType.RETRACTION -> {
                // A retraction supersedes all previous valid states in this thread
                existingEntries.forEach { 
                    relations.add(createRelation(it.id, newEntry.id, SupersessionType.RETRACTION, "Source has retracted information", now))
                }
            }
            TimelineEntryType.REGULATORY_ACTION, TimelineEntryType.RECOMMENDATION_CHANGE -> {
                // Authoritative updates supersede previous updates of the same type
                val previous = existingEntries.findLast { it.type == newEntry.type }
                if (previous != null) {
                    relations.add(createRelation(previous.id, newEntry.id, SupersessionType.REPLACEMENT, "Newer authoritative status established", now))
                }
            }
            else -> {}
        }

        return relations
    }

    private fun createRelation(
        previousId: String,
        supersedingId: String,
        type: SupersessionType,
        reason: String,
        timestamp: Instant
    ) = SupersessionRelation(
        id = UUID.randomUUID().toString(),
        previousEntryId = previousId,
        supersedingEntryId = supersedingId,
        type = type,
        reason = reason,
        detectedAt = timestamp
    )
}
