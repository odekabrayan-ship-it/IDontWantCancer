package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents a deterministic decision about the significance of an intelligence event.
 */
data class SignificanceDecision(
    val outcome: SignificanceOutcome,
    val reason: String,
    val factors: List<String>,
    val decidedAt: Instant
)
