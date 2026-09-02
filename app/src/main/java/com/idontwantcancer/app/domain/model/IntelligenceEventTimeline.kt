package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents the chronological evolution of an intelligence subject.
 * Derived from the underlying history of timeline entries.
 */
data class IntelligenceEventTimeline(
    val threadId: String,
    val topicIdentifier: String,
    val entries: List<TimelineEntry>
) {
    /**
     * Reconstructs the ordered timeline using the agency's deterministic ordering rules.
     */
    val sortedEntries: List<TimelineEntry> = entries.sortedWith(
        compareBy<TimelineEntry> { it.eventTime ?: INSTANT_MAX }
            .thenBy { it.sourceTime ?: INSTANT_MAX }
            .thenBy { it.publicationTime ?: INSTANT_MAX }
            .thenBy { it.ingestionTime }
            .thenBy { it.id }
    )
    
    private companion object {
        // Safe max instant for sorting logic
        private val INSTANT_MAX = Instant.parse("9999-12-31T23:59:59.999999999Z")
    }
}
