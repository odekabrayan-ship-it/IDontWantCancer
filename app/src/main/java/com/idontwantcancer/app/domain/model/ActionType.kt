package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the nature of the action recommended for a signal.
 */
@Serializable
enum class ActionType {
    /**
     * Immediate action to avoid a specific product or substance.
     */
    AVOID,

    /**
     * Clinical action recommended, such as getting a specific screening.
     */
    SCREEN,

    /**
     * Observational action: monitor for further updates or symptoms.
     */
    MONITOR,

    /**
     * Information only: no specific action required at this time.
     */
    NONE
}
