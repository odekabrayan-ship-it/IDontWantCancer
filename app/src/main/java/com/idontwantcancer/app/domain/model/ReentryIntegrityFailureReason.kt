package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines structured reasons for re-entry integrity failures.
 */
@Serializable
enum class ReentryIntegrityFailureReason {
    MISSING_HISTORY,
    INVALID_TRANSITION_HISTORY,
    STATE_MISMATCH,
    DUPLICATE_TRANSITION,
    AMBIGUOUS_ORDER,
    IDENTITY_MISMATCH,
    UNAUTHORIZED_TERMINAL_TRANSITION
}
