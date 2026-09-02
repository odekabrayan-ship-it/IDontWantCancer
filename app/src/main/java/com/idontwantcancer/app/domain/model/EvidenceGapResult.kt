package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an evidence gap analysis.
 */
@Serializable
data class EvidenceGapResult(
    val threadId: String,
    val level: EvidenceGapLevel,
    val reason: String,
    val affectedScope: String? = null,
    val relatedConflictId: String? = null,
    @Serializable(with = InstantSerializer::class)
    val analyzedAt: Instant
)
