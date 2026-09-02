package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The smallest, explicit, immutable contract describing a re-entry lifecycle 
 * event authorized for downstream consumption.
 */
@Serializable
data class IntelligenceReentryHandoffConsumptionContract(
    val reentryIdentity: String,
    val verifiedState: ReentryLifecycleState,
    val intelligenceId: String,
    val stateEntryId: String?,
    val transitionReason: String?,
    @Serializable(with = InstantSerializer::class)
    val admittedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val lastTransitionAt: Instant,
    val isTerminal: Boolean
)
