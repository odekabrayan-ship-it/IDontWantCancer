package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Duration
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceDeduplicationEngine] that uses deterministic
 * rules to identify and consolidate duplicate intelligence events.
 */
class DefaultIntelligenceDeduplicationEngine @Inject constructor() : IntelligenceDeduplicationEngine {

    override fun checkDeduplication(
        material: SourceMaterial,
        existingEvents: List<ConsolidatedEvent>
    ): DeduplicationResult {
        // 1. Precise source identity match (Internal deduplication)
        val idMatch = existingEvents.find { it.sourceMaterialIds.contains(material.contentId) }
        if (idMatch != null) {
            return DeduplicationResult(DeduplicationType.DUPLICATE_EVENT, idMatch.id)
        }

        // 2. Cross-source identity match via Canonical URL (if available)
        if (material.canonicalUrl != null) {
            val urlMatch = existingEvents.find { event ->
                // This is conceptual; in a real system we would need to know which 
                // materials are associated with which URLs in memory.
                // For now, we remain conservative.
                false 
            }
            if (urlMatch != null) {
                return DeduplicationResult(DeduplicationType.DUPLICATE_EVENT, (urlMatch as ConsolidatedEvent).id)
            }
        }

        // 3. Heuristic match: Same Title + Close Timing
        // We return POSSIBLE_DUPLICATE to avoid false merges, as per the conservative principle.
        val timingMatch = existingEvents.find {
            it.topicIdentifier.equals(material.title, ignoreCase = true) &&
            Duration.between(it.firstDetectedAt, material.publishedAt).abs().toHours() < 48
        }

        if (timingMatch != null) {
            return DeduplicationResult(DeduplicationType.POSSIBLE_DUPLICATE, timingMatch.id)
        }

        return DeduplicationResult(DeduplicationType.UNIQUE_EVENT)
    }

    override fun consolidate(
        material: SourceMaterial,
        existingEvent: ConsolidatedEvent?
    ): ConsolidatedEvent {
        val now = Instant.now()
        if (existingEvent == null) {
            return ConsolidatedEvent(
                id = UUID.randomUUID().toString(),
                topicIdentifier = material.title,
                sourceMaterialIds = listOf(material.contentId),
                firstDetectedAt = material.publishedAt,
                lastUpdatedAt = now
            )
        }

        // Preserve independent evidence by updating the source material ID list
        return existingEvent.copy(
            sourceMaterialIds = (existingEvent.sourceMaterialIds + material.contentId).distinct(),
            lastUpdatedAt = now
        )
    }
}
