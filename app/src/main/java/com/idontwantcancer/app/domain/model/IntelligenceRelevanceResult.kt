package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the structured result of a relevance and scope evaluation.
 */
@Serializable
data class IntelligenceRelevanceResult(
    val level: RelevanceLevel,
    val reason: String,
    val matchingThreadId: String? = null,
    val determinedScope: String? = null // Conceptual scope description
)
