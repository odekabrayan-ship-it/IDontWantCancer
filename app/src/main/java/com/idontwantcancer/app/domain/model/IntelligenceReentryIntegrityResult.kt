package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The structured result of a re-entry lifecycle recovery and integrity check.
 */
@Serializable
data class IntelligenceReentryIntegrityResult(
    val reentryIdentity: String,
    val status: ReentryIntegrityStatus,
    val storedState: ReentryLifecycleState?,
    val reconstructedState: ReentryLifecycleState?,
    val failureReasons: List<ReentryIntegrityFailureReason>,
    @Serializable(with = InstantSerializer::class)
    val verifiedAt: Instant
)
