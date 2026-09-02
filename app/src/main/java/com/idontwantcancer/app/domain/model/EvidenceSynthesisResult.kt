package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the result of synthesizing collective evidence for an intelligence thread.
 */
@Serializable
data class EvidenceSynthesisResult(
    val threadId: String,
    val level: EvidenceSynthesisLevel,
    val contributingSignalIds: List<String>,
    val conflictingSignalIds: List<String>,
    val reason: String,
    @Serializable(with = InstantSerializer::class)
    val synthesizedAt: Instant
)
