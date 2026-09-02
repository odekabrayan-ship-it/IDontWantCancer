package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command result acceptance by the receiving authority.
 */
@Serializable
enum class CommandAcceptanceStatus {
    /**
     * The receiving authority accepted the result as satisfying the contract.
     */
    ACCEPTED,

    /**
     * The receiving authority rejected the result (e.g. invalid contract).
     */
    REJECTED,

    /**
     * Acceptance is pending verification by the destination.
     */
    PENDING
}
