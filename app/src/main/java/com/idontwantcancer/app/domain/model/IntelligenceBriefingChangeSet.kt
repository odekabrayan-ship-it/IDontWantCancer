package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * A set of structured differences between a current briefing and its predecessor.
 */
data class IntelligenceBriefingChangeSet(
    val previousBriefingId: String?,
    val currentBriefingId: String,
    val itemChanges: List<IntelligenceBriefingItemChange>,
    val hasMeaningfulChange: Boolean,
    val comparedAt: Instant
)
