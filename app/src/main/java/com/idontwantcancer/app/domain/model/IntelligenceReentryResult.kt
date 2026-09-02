package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of the re-entry eligibility evaluation.
 */
@Serializable
data class IntelligenceReentryResult(
    val intelligenceId: String,
    val status: ReentryStatus,
    val triggers: List<ReentryTrigger>,
    val reason: String,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
