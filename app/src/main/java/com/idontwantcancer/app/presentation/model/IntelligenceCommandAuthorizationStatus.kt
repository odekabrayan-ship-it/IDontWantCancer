package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command dispatch authorization evaluation.
 */
@Serializable
enum class IntelligenceCommandAuthorizationStatus {
    /**
     * The action is formally permitted to proceed to execution.
     */
    AUTHORIZED,

    /**
     * The action is formally denied by the policy boundary.
     */
    DENIED,

    /**
     * Authorization is pending or could not be established.
     */
    PENDING
}
