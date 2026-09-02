package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an authoritative acknowledgement for a command result.
 */
@Serializable
data class IntelligenceCommandAcknowledgementResult(
    val operationId: String,
    val status: CommandAcknowledgementStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val acknowledgedAt: Instant = Instant.now()
)
