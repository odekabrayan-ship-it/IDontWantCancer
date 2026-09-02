package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured outcome of a communication readiness and safety evaluation.
 */
@Serializable
data class CommunicationReadinessResult(
    val level: CommunicationReadinessLevel,
    val reason: String,
    val reasonCategory: CommunicationSafetyReason = CommunicationSafetyReason.SATEISFIED,
    val requiresQualification: Boolean,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
