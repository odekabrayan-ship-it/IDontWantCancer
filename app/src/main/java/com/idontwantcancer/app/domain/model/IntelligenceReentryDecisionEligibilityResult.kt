package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of the outcome decision eligibility evaluation.
 */
@Serializable
data class IntelligenceReentryDecisionEligibilityResult(
    val reentryIdentity: String,
    val status: IntelligenceReentryDecisionEligibilityStatus,
    val reason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
