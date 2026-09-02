package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult

/**
 * Authoritative boundary for formalizing and mapping command-processing failures.
 */
interface IntelligenceCommandFailureBoundary {
    /**
     * Maps a throwable to a structured application command failure result.
     *
     * @param throwable The exception or error that occurred.
     * @param operationId The unique identifier of the failed operation.
     * @return The structured failure result.
     */
    fun mapFailure(
        throwable: Throwable,
        operationId: String? = null
    ): IntelligenceApplicationCommandResult.Failure
}
