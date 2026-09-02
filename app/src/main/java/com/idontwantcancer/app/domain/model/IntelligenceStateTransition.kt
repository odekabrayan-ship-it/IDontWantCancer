package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents a deterministic transition between intelligence states.
 * Preserves the logic and provenance of how the agency's understanding evolved.
 */
data class IntelligenceStateTransition(
    val id: String,
    val threadId: String,
    val previousStateEntryId: String?,
    val resultingStateEntryId: String,
    val type: IntelligenceStateTransitionType,
    val triggeringEventId: String, // References the TimelineEntry that caused the transition
    val effectiveAt: Instant,
    val reason: String
)
