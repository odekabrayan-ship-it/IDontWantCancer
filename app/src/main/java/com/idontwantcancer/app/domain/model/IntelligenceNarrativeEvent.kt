package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a structured turning point or milestone in the evolution 
 * of an intelligence situation.
 */
@Serializable
data class IntelligenceNarrativeEvent(
    val id: String,
    val threadId: String,
    val category: IntelligenceNarrativeEventCategory,
    val description: String,
    @Serializable(with = InstantSerializer::class)
    val effectiveTime: Instant,
    
    // Linkages to structured domain objects
    val timelineEntryId: String?,
    val signalId: String? = null,
    val transitionId: String? = null,
    val continuityLevel: ContinuityLevel? = null,
    val conflictId: String? = null,
    val supersessionRelationId: String? = null,
    
    val isCurrentState: Boolean = false
)
