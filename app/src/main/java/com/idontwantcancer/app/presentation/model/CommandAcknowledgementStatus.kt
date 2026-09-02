package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command result acknowledgement.
 */
@Serializable
enum class CommandAcknowledgementStatus {
    /**
     * The result was successfully delivered and acknowledged by the destination.
     */
    ACKNOWLEDGED,

    /**
     * The destination refused to acknowledge or accept the result.
     */
    REJECTED,

    /**
     * The result is pending acknowledgement.
     */
    PENDING
}
