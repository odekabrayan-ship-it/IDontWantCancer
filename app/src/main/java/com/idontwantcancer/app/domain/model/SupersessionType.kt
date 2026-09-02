package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the semantic nature of a supersession relationship.
 */
@Serializable
enum class SupersessionType {
    /**
     * The newer state completely replaces the previous one.
     */
    REPLACEMENT,

    /**
     * The newer entry is a formal correction of the previous one.
     */
    CORRECTION,

    /**
     * The newer entry is a formal retraction of the previous claim.
     */
    RETRACTION,

    /**
     * An official update that evolves the previous state.
     */
    OFFICIAL_UPDATE,

    /**
     * A change in status that supersedes the previous status.
     */
    STATUS_CHANGE
}
