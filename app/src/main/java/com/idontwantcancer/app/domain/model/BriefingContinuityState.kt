package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the structured continuity relationship for an intelligence item 
 * across successive briefings.
 */
@Serializable
enum class BriefingContinuityState {
    /**
     * Genuinely new intelligence not seen in previous briefings.
     */
    NEW,

    /**
     * Intelligence that continues to be relevant and authoritative with no change.
     */
    CONTINUING,

    /**
     * Intelligence that has received a compatible update.
     */
    UPDATED,

    /**
     * Intelligence that has undergone a material change in its state.
     */
    MATERIAL_CHANGE,

    /**
     * Intelligence that materially reverses a previous state.
     */
    REVERSAL,

    /**
     * Intelligence concerning a previously resolved topic that has been reactivated.
     */
    REACTIVATED,

    /**
     * Intelligence that formally resolves an active topic.
     */
    RESOLVED,

    /**
     * Intelligence that has been superseded by newer information.
     */
    SUPERSEDED,

    /**
     * Older intelligence preserved for historical context.
     */
    HISTORICAL
}
