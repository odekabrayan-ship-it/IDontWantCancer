package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a post-transition lifecycle verification.
 */
@Serializable
enum class ReentryPostTransitionVerificationStatus {
    /**
     * The authoritative state perfectly matches the expected post-transition state.
     */
    VERIFIED,

    /**
     * A mismatch was detected between the authoritative state and the expected state.
     */
    INCONSISTENT,

    /**
     * The re-entry event record was not found during verification.
     */
    EVENT_NOT_FOUND
}
