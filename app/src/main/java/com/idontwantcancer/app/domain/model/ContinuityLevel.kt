package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the longitudinal relationship between a new intelligence unit 
 * and the existing intelligence context.
 */
@Serializable
enum class ContinuityLevel {
    /**
     * Newly processed intelligence that carries the same authoritative 
     * status as the previous state with no material change.
     */
    NO_MATERIAL_CHANGE,

    /**
     * Continuous tracking of an ongoing development.
     */
    CONTINUATION,

    /**
     * A meaningful but compatible update to existing knowledge.
     */
    UPDATE,

    /**
     * A significant shift in the established understanding or status.
     */
    MATERIAL_CHANGE,

    /**
     * A move in the opposite direction from the previously established state.
     */
    REVERSAL,

    /**
     * New intelligence that revives a previously resolved thread.
     */
    REACTIVATED,

    /**
     * Intelligence providing purely historical context without altering current state.
     */
    HISTORICAL_CONTEXT,

    /**
     * Insufficient historical data exists to establish continuity.
     */
    INSUFFICIENT_CONTEXT
}
