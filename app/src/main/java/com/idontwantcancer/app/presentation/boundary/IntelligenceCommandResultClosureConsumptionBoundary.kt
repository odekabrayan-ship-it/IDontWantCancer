package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary through which downstream consumers process 
 * result-delivery closure observations.
 */
interface IntelligenceCommandResultClosureConsumptionBoundary {
    /**
     * A stream of command closure results for consumption.
     * Consumers use this to react to the terminal state of specific command deliveries.
     */
    val closureStream: Flow<IntelligenceCommandClosureResult>

    /**
     * Consumes the closure status for a specific operation.
     *
     * @param operationId The unique identity of the operation.
     * @return The authoritative closure status if available.
     */
    suspend fun consumeClosure(operationId: String): IntelligenceCommandClosureResult?
}
