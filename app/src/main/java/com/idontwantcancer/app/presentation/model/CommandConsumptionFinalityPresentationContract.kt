package com.idontwantcancer.app.presentation.model

/**
 * A safe, immutable presentation contract for command consumption finality.
 * Refines the Step 134 projection for safe UI consumption.
 */
sealed interface CommandConsumptionFinalityPresentationContract {
    /**
     * The unique identity of the command operation.
     */
    val operationId: String

    /**
     * Downstream consumption has reached its authoritative terminal state.
     */
    data class Final(
        override val operationId: String,
        val detail: String?
    ) : CommandConsumptionFinalityPresentationContract

    /**
     * Downstream consumption is still in progress or waiting.
     */
    data class NonTerminal(
        override val operationId: String,
        val detail: String?
    ) : CommandConsumptionFinalityPresentationContract

    /**
     * Finality status is currently unknown or unavailable.
     */
    data object StatusUnavailable : CommandConsumptionFinalityPresentationContract {
        override val operationId: String = ""
    }
}
