package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The structured terminal result of a re-entry lifecycle transition pipeline.
 */
@Serializable
data class IntelligenceReentryTransitionCompletionResult(
    val reentryIdentity: String,
    val status: ReentryTransitionCompletionStatus,
    val verificationResult: IntelligenceReentryPostTransitionVerificationResult,
    @Serializable(with = InstantSerializer::class)
    val completedAt: Instant
)
