package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents the structured result of the briefing selection process.
 */
data class IntelligenceBriefingSelection(
    val id: String,
    val orderedSignalIds: List<String>,
    val selectedPackages: Map<String, IntelligenceCommunicationPackage>,
    val excludedSignals: Map<String, SelectionExclusionReason>,
    val selectedAt: Instant
)
