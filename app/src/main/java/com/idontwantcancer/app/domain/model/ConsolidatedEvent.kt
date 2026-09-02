package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents an underlying real-world event reported by one or more sources.
 * This supports the consolidation of multiple reports into a single intelligence unit.
 */
@Serializable
data class ConsolidatedEvent(
    val id: String,
    val topicIdentifier: String,
    val sourceMaterialIds: List<String>,
    @Serializable(with = InstantSerializer::class)
    val firstDetectedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val lastUpdatedAt: Instant,
    val threadId: String? = null
)
