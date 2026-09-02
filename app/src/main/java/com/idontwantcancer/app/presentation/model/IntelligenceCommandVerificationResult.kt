package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of an authoritative verification of a command result.
 */
@Serializable
data class IntelligenceCommandVerificationResult(
    val operationId: String,
    val status: CommandVerificationStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val verifiedAt: Instant = Instant.now()
)
