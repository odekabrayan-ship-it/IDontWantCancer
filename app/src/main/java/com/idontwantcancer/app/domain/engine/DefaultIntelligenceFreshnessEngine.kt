package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of [IntelligenceFreshnessEngine] that uses deterministic
 * rules to evaluate the temporal status of intelligence items.
 */
class DefaultIntelligenceFreshnessEngine @Inject constructor() : IntelligenceFreshnessEngine {

    override fun evaluateFreshness(
        entry: TimelineEntry,
        timeline: IntelligenceEventTimeline,
        thread: IntelligenceThread
    ): FreshnessStatus {
        val now = Instant.now()
        val sorted = timeline.sortedEntries
        
        // 1. Identify effective time
        val effectiveTime = entry.eventTime ?: entry.sourceTime ?: entry.publicationTime ?: entry.ingestionTime

        // 2. Check for explicit supersession or correction in later chronological entries
        val laterEntries = sorted.filter { 
            val itTime = it.eventTime ?: it.sourceTime ?: it.publicationTime ?: it.ingestionTime
            itTime.isAfter(effectiveTime) || (itTime == effectiveTime && it.id != entry.id)
        }
        
        val supersedingEntry = laterEntries.find { 
            it.type == TimelineEntryType.CORRECTION || 
            it.type == TimelineEntryType.REVERSAL ||
            it.type == TimelineEntryType.MATERIAL_CHANGE ||
            it.type == TimelineEntryType.REGULATORY_ACTION ||
            it.type == TimelineEntryType.RESOLUTION ||
            it.type == TimelineEntryType.RECOMMENDATION_CHANGE
        }
        
        if (supersedingEntry != null) {
            val level = if (supersedingEntry.type == TimelineEntryType.RESOLUTION) {
                FreshnessLevel.HISTORICAL
            } else {
                FreshnessLevel.STALE
            }

            return FreshnessStatus(
                level = level,
                effectiveSince = effectiveTime,
                supersededByEntryId = supersedingEntry.id,
                reason = "Intelligence superseded by later event: ${supersedingEntry.type}",
                evaluatedAt = now
            )
        }

        // 3. Evaluate based on Thread context
        // If the topic is resolved, non-terminal entries are historical.
        // Terminal entries (RESOLUTION) follow normal aging.
        val isResolved = sorted.any { it.type == TimelineEntryType.RESOLUTION }
        if (isResolved && entry.type != TimelineEntryType.RESOLUTION) {
             return FreshnessStatus(
                level = FreshnessLevel.HISTORICAL,
                effectiveSince = effectiveTime,
                reason = "Topic has been formally resolved in the intelligence record.",
                evaluatedAt = now
            )
        }

        // 4. Age-based characterization (Heuristic, not quality judgment)
        val ageDays = Duration.between(effectiveTime, now).toDays()
        
        val level = when {
            // Regulatory and Recommendation changes stay current longer
            entry.type == TimelineEntryType.REGULATORY_ACTION || 
            entry.type == TimelineEntryType.RECOMMENDATION_CHANGE ||
            entry.type == TimelineEntryType.RESOLUTION -> {
                if (ageDays > 730) FreshnessLevel.HISTORICAL 
                else if (ageDays > 365) FreshnessLevel.AGING 
                else FreshnessLevel.CURRENT
            }
            // General observations and evidence age faster
            ageDays > 365 -> FreshnessLevel.HISTORICAL
            ageDays > 180 -> FreshnessLevel.AGING
            else -> FreshnessLevel.CURRENT
        }

        return FreshnessStatus(
            level = level,
            effectiveSince = effectiveTime,
            reason = "Intelligence represents the most recent state of knowledge.",
            evaluatedAt = now
        )
    }
}
