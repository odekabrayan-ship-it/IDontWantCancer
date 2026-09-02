package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the high-level status of a reconciliation between a historical
 * communication cycle and the current intelligence domain.
 */
@Serializable
enum class CommunicationReconciliationStatus {
    /**
     * No meaningful differences detected between the snapshot and current intelligence.
     */
    UNCHANGED,

    /**
     * One or more items in the cycle have materially changed or been superseded.
     */
    CHANGED,

    /**
     * Minor updates or priority shifts detected without material intelligence change.
     */
    PARTIALLY_CHANGED,

    /**
     * The intelligence represented in the cycle is no longer considered current.
     */
    NO_LONGER_CURRENT,

    /**
     * One or more primary intelligence references from the cycle are missing in the current domain.
     */
    CURRENT_REFERENCE_MISSING
}
