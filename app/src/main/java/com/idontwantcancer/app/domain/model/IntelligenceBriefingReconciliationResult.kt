package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The structured outcome of reconciling a historical briefing cycle with 
 * the current intelligence domain.
 */
@Serializable
data class IntelligenceBriefingReconciliationResult(
    val briefingId: String,
    val snapshotId: String,
    val status: CommunicationReconciliationStatus,
    val itemDiffs: List<BriefingItemReconciliationDiff>,
    @Serializable(with = InstantSerializer::class)
    val reconciledAt: Instant
)
