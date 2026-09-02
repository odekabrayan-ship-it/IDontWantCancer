package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult

/**
 * Interface for the component responsible for formalizing the results of 
 * authoritative application commands.
 */
interface IntelligenceCommandResultBoundary {
    /**
     * Formalizes a successful operation into a command result.
     *
     * @param operationId The unique identifier of the operation.
     * @return The structured success result.
     */
    fun success(operationId: String? = null): IntelligenceApplicationCommandResult.Success

    /**
     * Formalizes an operation failure into a command result.
     *
     * @param reason The reason for the failure.
     * @param operationId The unique identifier of the operation.
     * @return The structured failure result.
     */
    fun failure(reason: String, operationId: String? = null): IntelligenceApplicationCommandResult.Failure

    /**
     * Formalizes a domain rejection into a command result.
     *
     * @param reason The reason for the rejection.
     * @param operationId The unique identifier of the operation.
     * @return The structured rejection result.
     */
    fun rejected(reason: String, operationId: String? = null): IntelligenceApplicationCommandResult.Rejected

    /**
     * Formalizes a command cancellation into a command result.
     *
     * @param operationId The unique identifier of the operation.
     * @return The structured cancellation result.
     */
    fun cancelled(operationId: String? = null): IntelligenceApplicationCommandResult.Cancelled

    /**
     * Formalizes a command timeout into a command result.
     *
     * @param operationId The unique identifier of the operation.
     * @return The structured timeout result.
     */
    fun timedOut(operationId: String? = null): IntelligenceApplicationCommandResult.TimedOut
}
