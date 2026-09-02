package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command retry evaluation.
 */
@Serializable
enum class CommandRetryStatus {
    /**
     * The command is eligible for retry according to existing policy.
     */
    RETRY_PERMITTED,

    /**
     * The command is not eligible for retry (e.g. permanent failure or limit reached).
     */
    RETRY_PROHIBITED,

    /**
     * Retry is managed by an external authority (e.g. WorkManager or the User).
     */
    EXTERNALLY_MANAGED
}
