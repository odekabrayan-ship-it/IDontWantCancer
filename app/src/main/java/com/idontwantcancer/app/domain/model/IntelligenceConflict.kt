package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a detected disagreement between multiple pieces of intelligence.
 * This supports the agency's ability to maintain a transparent and auditable record
 * of conflicting evidence.
 */
@Serializable
data class IntelligenceConflict(
    val id: String,
    val topicIdentifier: String, // Stable identifier for the subject of the conflict
    val participatingSourceIds: List<String>,
    val type: ConflictType,
    val resolutionStatus: ResolutionStatus,
    val competingSignalIds: List<String>,
    @Serializable(with = InstantSerializer::class)
    val detectedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val resolvedAt: Instant? = null,
    val resolutionReasoning: String? = null
)
