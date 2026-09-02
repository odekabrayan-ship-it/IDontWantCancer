package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a specific structured factor used to evaluate evidence strength.
 * This supports auditability and transparency in the intelligence pipeline.
 */
@Serializable
data class EvidenceFactor(
    val name: String,
    val description: String,
    val impact: FactorImpact
)

/**
 * Defines how a factor influenced the evidence assessment.
 */
@Serializable
enum class FactorImpact {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}
