package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the authoritative lifecycle states of a specific re-entry event.
 */
@Serializable
enum class ReentryLifecycleState {
    /**
     * Re-entry has been authorized and admitted to the evaluation pipeline.
     */
    ADMITTED,

    /**
     * Intelligence is currently undergoing re-evaluation (significance, evidence, etc).
     */
    PROCESSING,

    /**
     * Re-entry resulted in a new communication (Briefing Item created).
     */
    COMPLETED,

    /**
     * Re-entry was rejected during evaluation (e.g. not significant enough).
     */
    REJECTED,

    /**
     * This re-entry event has been superseded by a newer re-entry event.
     */
    SUPERSEDED
}
