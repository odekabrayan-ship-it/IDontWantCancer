package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an authoritative rejection of a command result.
 */
@Serializable
data class IntelligenceCommandRejectionResult(
    val operationId: String,
    val reason: String,
    @Serializable(with = InstantSerializer::class)
    val rejectedAt: Instant = Instant.now()
)
