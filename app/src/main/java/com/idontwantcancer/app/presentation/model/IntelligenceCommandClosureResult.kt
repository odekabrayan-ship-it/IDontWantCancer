package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of a closure evaluation for a command result delivery.
 */
@Serializable
data class IntelligenceCommandClosureResult(
    val operationId: String,
    val status: CommandResultClosureStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant = Instant.now()
)
