package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents the structured result of the communication eligibility selection process.
 */
data class IntelligenceCommunicationSelectionResult(
    val selectedHandoffs: List<IntelligenceCommunicationHandoff>,
    val excludedHandoffs: Map<String, String>, // intelligenceId to reason
    val selectedAt: Instant
)
