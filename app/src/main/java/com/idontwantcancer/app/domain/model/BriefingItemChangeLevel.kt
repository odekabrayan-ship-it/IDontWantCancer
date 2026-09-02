package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the classification of change for an item in a briefing 
 * relative to the previous briefing.
 */
@Serializable
enum class BriefingItemChangeLevel {
    /**
     * No change in the intelligence state or its representation.
     */
    UNCHANGED,

    /**
     * A genuinely new item that was not present in the previous briefing.
     */
    NEW,

    /**
     * The item was present but has meaningful compatible updates.
     */
    UPDATED,

    /**
     * The underlying intelligence has undergone a material state change.
     */
    MATERIALLY_CHANGED,

    /**
     * The item represents a reactivated topic that was previously resolved.
     */
    REACTIVATED,

    /**
     * The item was present but is now superseded by other intelligence.
     */
    SUPERSEDED,

    /**
     * The item was removed from the briefing for structural reasons.
     */
    REMOVED_FROM_BRIEFING
}
