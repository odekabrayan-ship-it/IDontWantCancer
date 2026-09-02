package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a deterministic, append-only record of a re-entry lifecycle transition.
 * Supports a complete audit trail of the agency's re-evaluation process.
 */
@Serializable
data class IntelligenceReentryAuditEntry(
    val reentryIdentity: String,
    val sequenceNumber: Int,
    val previousState: ReentryLifecycleState?,
    val newState: ReentryLifecycleState,
    val transitionReason: String?,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant
)
