package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a single chronological state or event in the evolution of an intelligence topic.
 * Preserves what happened, when it happened, and which source established it.
 */
@Serializable
data class TimelineEntry(
    val id: String,
    val threadId: String,
    val type: TimelineEntryType,
    val description: String,
    
    // Temporal context
    @Serializable(with = InstantSerializer::class)
    val eventTime: Instant? = null,        // When the underlying event occurred
    @Serializable(with = InstantSerializer::class)
    val sourceTime: Instant? = null,       // Source's own timestamp for the event
    @Serializable(with = InstantSerializer::class)
    val publicationTime: Instant? = null,  // When it was reported to the world
    @Serializable(with = InstantSerializer::class)
    val ingestionTime: Instant,            // When the agency received it
    
    // Provenance
    val sourceMaterialId: String? = null,
    val changeId: String? = null,
    val signalId: String? = null,
    
    // State reconstruction
    val stateSnapshot: String? = null      // Opaque summary of state at this point
)
