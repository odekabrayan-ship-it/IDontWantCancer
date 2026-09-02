package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command result-delivery lifecycle closure.
 */
@Serializable
enum class CommandResultClosureStatus {
    /**
     * The result-delivery lifecycle is actively processing (open).
     */
    OPEN,

    /**
     * The result-delivery lifecycle has reached a terminal state (closed).
     */
    CLOSED
}
