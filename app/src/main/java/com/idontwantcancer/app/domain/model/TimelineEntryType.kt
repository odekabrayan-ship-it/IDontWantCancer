package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the nature of a specific entry in an intelligence timeline.
 */
@Serializable
enum class TimelineEntryType {
    /**
     * The very first observed event or signal for this topic.
     */
    INITIAL_OBSERVATION,

    /**
     * A significant change in scientific evidence or reporting.
     */
    MATERIAL_CHANGE,

    /**
     * Official guidance or recommendations have changed.
     */
    RECOMMENDATION_CHANGE,

    /**
     * An official regulatory or public health action has been taken.
     */
    REGULATORY_ACTION,

    /**
     * A previous finding or recommendation has been reversed.
     */
    REVERSAL,

    /**
     * A formal correction to previously issued information.
     */
    CORRECTION,

    /**
     * Official confirmation of previously preliminary information.
     */
    OFFICIAL_CONFIRMATION,

    /**
     * New evidence that strengthens an existing observation.
     */
    EVIDENCE_UPGRADE,
    
    RESOLUTION,
    
    RETRACTION
}
