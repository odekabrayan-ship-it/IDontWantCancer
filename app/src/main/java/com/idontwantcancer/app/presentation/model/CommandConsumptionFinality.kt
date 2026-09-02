package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the finality status of a downstream consumption lifecycle.
 */
@Serializable
enum class CommandConsumptionFinality {
    /**
     * The downstream consumption is still in progress or waiting for further events.
     */
    NON_TERMINAL,

    /**
     * The downstream consumption has reached its own authoritative terminal state.
     */
    TERMINAL
}
