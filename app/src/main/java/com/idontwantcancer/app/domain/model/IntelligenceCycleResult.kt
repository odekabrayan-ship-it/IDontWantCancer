package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents the outcome of an autonomous intelligence cycle.
 */
data class IntelligenceCycleResult(
    val cycleTimestamp: Instant,
    val sourcesCheckedCount: Int,
    val newSourceItemsCount: Int,
    val detectedChangesCount: Int,
    val significantChangesCount: Int,
    val signalsFormedCount: Int,
    val briefing: IntelligenceBriefing?,
    val integrityResult: IntelligenceIntegrityResult? = null,
    val failures: List<CycleFailure> = emptyList()
)

/**
 * Represents a failure that occurred during a specific part of the intelligence cycle.
 */
data class CycleFailure(
    val sourceId: String?,
    val stage: String,
    val message: String
)
