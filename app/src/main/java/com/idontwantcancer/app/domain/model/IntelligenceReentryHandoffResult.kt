package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of a re-entry lifecycle handoff.
 */
@Serializable
data class IntelligenceReentryHandoffResult(
    val reentryIdentity: String,
    val status: IntelligenceReentryHandoffStatus,
    val contract: IntelligenceReentryHandoffConsumptionContract?,
    val rejectionReason: String? = null,
    @Serializable(with = InstantSerializer::class)
    val handedOffAt: Instant
)
