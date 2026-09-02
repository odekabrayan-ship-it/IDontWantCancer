package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured temporal state of an intelligence item.
 */
@Serializable
data class FreshnessStatus(
    val level: FreshnessLevel,
    @Serializable(with = InstantSerializer::class)
    val effectiveSince: Instant?,
    val supersededByEntryId: String? = null,
    val reason: String,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
