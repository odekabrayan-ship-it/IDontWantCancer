package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the final receiving-side disposition of a command result.
 */
@Serializable
enum class CommandResultDisposition {
    /**
     * The result was acknowledged but final acceptance is not yet established.
     */
    ACKNOWLEDGED,

    /**
     * The result was accepted as satisfying the receiving contract.
     */
    ACCEPTED,

    /**
     * The result was rejected by the receiving authority.
     */
    REJECTED,

    /**
     * The final disposition cannot currently be established (uncertain state).
     */
    UNRESOLVED
}
