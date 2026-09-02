package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary through which already-finalized command results 
 * are consumed by downstream components.
 */
interface IntelligenceCommandResultConsumptionBoundary {
    /**
     * A stream of published command results for consumption.
     */
    val resultStream: Flow<IntelligenceApplicationCommandResult>

    /**
     * Consumes a specific published command result.
     *
     * @param operationId The unique identity of the operation.
     * @return The authoritative command result if available.
     */
    suspend fun consumeResult(operationId: String): IntelligenceApplicationCommandResult?
}
