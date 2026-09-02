package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of a change continuity evaluation.
 */
@Serializable
data class IntelligenceContinuityResult(
    val level: ContinuityLevel,
    val threadId: String,
    val signalId: String?,
    val previousStateEntryId: String?,
    val reason: String,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
