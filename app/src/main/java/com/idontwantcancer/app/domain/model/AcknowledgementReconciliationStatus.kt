package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the high-level status of an acknowledgement reconciliation.
 */
@Serializable
enum class AcknowledgementReconciliationStatus {
    /**
     * The acknowledgement correctly corresponds to an authorized consumption event.
     */
    RECONCILED,

    /**
     * The acknowledgement does not correspond to an expected consumption event.
     */
    NOT_RECONCILED
}
