package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the structured outcome of a requested re-entry lifecycle state transition.
 */
sealed interface ReentryTransitionResult {
    /**
     * The transition was accepted and the state updated.
     */
    data class Accepted(val updatedLifecycle: IntelligenceReentryLifecycle) : ReentryTransitionResult

    /**
     * The transition was rejected due to domain integrity rules.
     */
    data class Rejected(
        val reason: ReentryTransitionRejectionReason,
        val currentState: ReentryLifecycleState,
        val requestedState: ReentryLifecycleState
    ) : ReentryTransitionResult
}

/**
 * Defines structured reasons for rejecting a state transition.
 */
@Serializable
enum class ReentryTransitionRejectionReason {
    /**
     * The requested transition is not defined in the lifecycle graph.
     */
    INVALID_TRANSITION,

    /**
     * The current state is terminal and cannot be transitioned out of.
     */
    TERMINAL_STATE,

    /**
     * The re-entry event record was not found.
     */
    EVENT_NOT_FOUND
}
