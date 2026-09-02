package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the structured result of the re-entry deduplication gate.
 */
@Serializable
data class IntelligenceReentryDeduplicationResult(
    val reentryIdentity: String, // Deterministic identity (e.g. signalId + stateId)
    val status: DeduplicationStatus,
    val intelligenceId: String,
    val stateEntryId: String?,
    val reason: String,
    @Serializable(with = InstantSerializer::class)
    val evaluatedAt: Instant
)
