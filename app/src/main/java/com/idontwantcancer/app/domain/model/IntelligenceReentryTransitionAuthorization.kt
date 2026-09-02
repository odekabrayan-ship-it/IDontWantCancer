package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a formally authorized transition for a re-entry event.
 * This is the Step 97 product required for execution.
 */
@Serializable
data class IntelligenceReentryTransitionAuthorization(
    val updatedLifecycle: IntelligenceReentryLifecycle,
    val previousState: ReentryLifecycleState,
    val reason: String?,
    @Serializable(with = InstantSerializer::class)
    val authorizedAt: Instant
)
