package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command result publication.
 */
@Serializable
enum class CommandPublicationStatus {
    /**
     * The result was successfully published to authorized consumers.
     */
    PUBLISHED,

    /**
     * The publication was suppressed or refused (e.g. unverified result).
     */
    SUPPRESSED,

    /**
     * Publication is pending.
     */
    PENDING
}
