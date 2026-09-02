package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the formal stages of an application command's operational lifecycle.
 */
@Serializable
enum class CommandLifecycleStage {
    /**
     * The command has been received by the interaction boundary.
     */
    RECEIVED,

    /**
     * The command has been accepted for processing.
     */
    ACCEPTED,

    /**
     * The command has been routed to its authoritative handler.
     */
    DISPATCHED,

    /**
     * The authoritative handler is actively processing the command.
     */
    PROCESSING,

    /**
     * Processing has concluded and a result is available.
     */
    COMPLETED,

    /**
     * The command was cancelled by the caller.
     */
    CANCELLED,

    /**
     * The command exceeded its authoritative time limit.
     */
    TIMED_OUT
}
