package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the final status of a re-entry lifecycle transition attempt.
 */
@Serializable
enum class ReentryTransitionCompletionStatus {
    /**
     * The transition successfully completed and was verified.
     */
    COMPLETED,

    /**
     * The transition failed to complete or verification was inconsistent.
     */
    NOT_COMPLETED
}
