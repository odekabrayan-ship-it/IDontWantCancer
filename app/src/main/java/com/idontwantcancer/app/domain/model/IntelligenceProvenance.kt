package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * A complete, traceable record of an intelligence item's lineage.
 * Allows for auditing why a piece of intelligence exists and how its 
 * characterization was reached.
 */
@Serializable
data class IntelligenceProvenance(
    val signalId: String,
    val signal: Signal,
    val thread: IntelligenceThread?,
    val timelineEntries: List<TimelineEntry>,
    val sources: List<SourceMaterial>,
    val consolidatedEvent: ConsolidatedEvent?,
    val briefingItem: IntelligenceBriefingItem? = null,
    @Serializable(with = InstantSerializer::class)
    val reconstructedAt: Instant
)
