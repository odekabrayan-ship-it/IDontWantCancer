package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a sequence of related intelligence events over time.
 * This allows the agency to track the evolution of a specific topic.
 */
@Serializable
data class IntelligenceThread(
    val id: String,
    val topicIdentifier: String,
    @Serializable(with = InstantSerializer::class)
    val firstDetectedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val lastUpdatedAt: Instant,
    val currentStatus: String? = null,
    val signalIds: List<String> = emptyList()
)
