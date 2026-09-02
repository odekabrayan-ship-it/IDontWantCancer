package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command reconciliation evaluation.
 */
@Serializable
enum class IntelligenceCommandReconciliationStatus {
    /**
     * The authoritative state confirms that the operation was successful.
     */
    CONFIRMED_SUCCESS,

    /**
     * The authoritative state confirms that the operation did not result in the intended change.
     */
    CONFIRMED_FAILURE,

    /**
     * The authoritative state cannot be established (ambiguous outcome).
     */
    INDETERMINATE,

    /**
     * The system state has been successfully reconciled with the command attempt.
     */
    RECONCILED
}
