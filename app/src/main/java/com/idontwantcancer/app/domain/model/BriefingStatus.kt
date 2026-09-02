package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the readiness and nature of an intelligence briefing.
 */
@Serializable
enum class BriefingStatus {
    /**
     * The briefing contains meaningful intelligence signals ready for presentation.
     */
    READY,

    /**
     * No meaningful changes were detected that met the significance threshold for a briefing.
     */
    NO_MAJOR_CHANGES,

    /**
     * The briefing contains items requiring immediate user awareness or action.
     */
    ATTENTION_REQUIRED
}
