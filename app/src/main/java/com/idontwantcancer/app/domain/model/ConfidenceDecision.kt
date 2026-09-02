package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents a deterministic decision about the confidence level of intelligence evidence.
 */
data class ConfidenceDecision(
    val confidence: SignalConfidence,
    val factors: List<EvidenceFactor>,
    val assessedAt: Instant,
    val hasUnresolvedConflicts: Boolean = false
)
