package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an idempotency evaluation for an application command.
 */
@Serializable
data class IntelligenceCommandIdempotencyResult(
    val commandIdentity: String,
    val status: CommandIdempotencyStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
