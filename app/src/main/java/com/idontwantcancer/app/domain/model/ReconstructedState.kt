package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the agency's current understanding of an intelligence topic, 
 * derived from its historical timeline.
 */
@Serializable
data class ReconstructedState(
    val threadId: String,
    val summary: String,
    val effectiveEntryId: String,
    val confidence: SignalConfidence? = null,
    val conflictStatus: ResolutionStatus? = null,
    val lastMeaningfulChangeId: String? = null,
    @Serializable(with = InstantSerializer::class)
    val reconstructedAt: Instant
)
