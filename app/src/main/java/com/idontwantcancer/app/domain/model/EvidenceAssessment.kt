package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents the structured evaluation of evidence behind a detected change.
 * This is an intermediate intelligence object used before signal generation.
 */
data class EvidenceAssessment(
    val change: DetectedChange,
    val strength: EvidenceStrength,
    val factors: List<EvidenceFactor>,
    val assessedAt: Instant,
    val isConsistent: Boolean = true,
    val hasUncertainty: Boolean = false
)
