package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * A deterministic container representing one communication cycle.
 * Bundles a selected and ordered set of validated intelligence handoffs.
 * Acts as the authoritative domain contract for a single batch of communication.
 */
data class IntelligenceCommunicationCycle(
    val id: String,
    val timestamp: Instant,
    val selectedHandoffs: List<IntelligenceCommunicationHandoff>,
    val status: BriefingStatus
)
