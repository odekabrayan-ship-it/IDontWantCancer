package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents the authoritative lifecycle record of a re-entry event.
 */
@Serializable
data class IntelligenceReentryLifecycle(
    val reentryIdentity: String, // signalId::stateEntryId
    val currentState: ReentryLifecycleState,
    val intelligenceId: String,
    val stateEntryId: String?,
    @Serializable(with = InstantSerializer::class)
    val admittedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val lastTransitionAt: Instant,
    val transitionReason: String? = null
)
