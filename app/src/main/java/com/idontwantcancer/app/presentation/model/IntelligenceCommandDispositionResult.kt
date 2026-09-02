package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured final disposition of a command result on the receiving side.
 */
@Serializable
data class IntelligenceCommandDispositionResult(
    val operationId: String,
    val disposition: CommandResultDisposition,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant = Instant.now()
)
