package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a consumer's acknowledgement of a re-entry contract.
 */
@Serializable
enum class ReentryAcknowledgementStatus {
    /**
     * The consumer successfully accepted and processed the contract.
     */
    ACCEPTED,

    /**
     * The consumer rejected the contract for processing.
     */
    REJECTED
}
