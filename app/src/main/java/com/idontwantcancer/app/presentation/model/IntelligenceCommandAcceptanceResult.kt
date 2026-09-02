package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an authoritative acceptance evaluation for a command result.
 */
@Serializable
data class IntelligenceCommandAcceptanceResult(
    val operationId: String,
    val status: CommandAcceptanceStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant = Instant.now()
)
