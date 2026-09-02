package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the presentation priority or attention requirement for a Signal.
 * This is used to determine how prominently a Signal is displayed to the user.
 */
@Serializable
enum class AttentionLevel {
    /**
     * Requires immediate attention. Should be prominently featured in the daily briefing.
     */
    IMMEDIATE,

    /**
     * Important information that the user should be aware of soon.
     */
    IMPORTANT,

    /**
     * Routine intelligence update. Available for review but not elevated.
     */
    ROUTINE,

    /**
     * Historical or non-urgent intelligence. Kept for reference.
     */
    ARCHIVE
}
