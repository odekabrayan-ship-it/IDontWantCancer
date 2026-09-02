package com.idontwantcancer.app.presentation.model

/**
 * Represents the raw outcome produced by an execution authority.
 * This is the input to the authoritative command result established in Step 142.
 */
sealed interface IntelligenceCommandExecutionOutcome {
    /**
     * The operation ran to completion without technical failure.
     */
    data class Success(val operationId: String? = null) : IntelligenceCommandExecutionOutcome

    /**
     * The operation failed due to a technical or logical error.
     */
    data class Failure(val throwable: Throwable, val operationId: String? = null) : IntelligenceCommandExecutionOutcome

    /**
     * The operation was rejected by domain policy.
     */
    data class Rejected(val reason: String, val operationId: String? = null) : IntelligenceCommandExecutionOutcome

    /**
     * The operation was explicitly cancelled.
     */
    data class Cancelled(val operationId: String? = null) : IntelligenceCommandExecutionOutcome

    /**
     * The operation timed out.
     */
    data class TimedOut(val operationId: String? = null) : IntelligenceCommandExecutionOutcome
}
