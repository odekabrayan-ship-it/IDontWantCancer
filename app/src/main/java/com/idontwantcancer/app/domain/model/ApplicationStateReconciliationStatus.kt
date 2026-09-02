package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a reconciliation between re-entry completion 
 * and the existing application state.
 */
@Serializable
enum class ApplicationStateReconciliationStatus {
    /**
     * The application state is consistent with the authoritative completion result.
     */
    CONSISTENT,

    /**
     * A discrepancy exists between the application state and the completion result.
     */
    INCONSISTENT
}
