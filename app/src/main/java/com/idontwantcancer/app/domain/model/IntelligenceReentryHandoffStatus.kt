package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a re-entry lifecycle handoff.
 */
@Serializable
enum class IntelligenceReentryHandoffStatus {
    /**
     * The re-entry context was successfully handed off.
     */
    HANDOFF_ACCEPTED,

    /**
     * The handoff was rejected due to lack of authorization or integrity failure.
     */
    HANDOFF_REJECTED
}
