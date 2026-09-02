package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The structured result of a post-transition lifecycle verification check.
 */
@Serializable
data class IntelligenceReentryPostTransitionVerificationResult(
    val reentryIdentity: String,
    val status: ReentryPostTransitionVerificationStatus,
    val expectedState: ReentryLifecycleState?,
    val actualState: ReentryLifecycleState?,
    val mismatchDetails: String? = null,
    @Serializable(with = InstantSerializer::class)
    val verifiedAt: Instant
)
