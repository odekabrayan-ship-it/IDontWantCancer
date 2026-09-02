package com.idontwantcancer.app.domain.model

/**
 * Defines the temporal status of a piece of intelligence relative to the present.
 */
enum class FreshnessLevel {
    /**
     * Represents the currently active and authoritative understanding.
     */
    CURRENT,

    /**
     * Still relevant but showing signs of age; new developments may be expected.
     */
    AGING,

    /**
     * Superseded by newer information or invalidated by a correction/retraction.
     */
    STALE,

    /**
     * Long-term established knowledge that has entered the historical record.
     */
    HISTORICAL
}
