package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * Default implementation of the command closure consumption boundary.
 * Provides a secure access point for downstream consumers to interact with closure facts.
 */
class DefaultIntelligenceCommandResultClosureConsumptionBoundary @Inject constructor(
    private val observationBoundary: IntelligenceCommandResultClosureObservationBoundary
) : IntelligenceCommandResultClosureConsumptionBoundary {

    override val closureStream: Flow<IntelligenceCommandClosureResult> = 
        observationBoundary.closureStream

    override suspend fun consumeClosure(operationId: String): IntelligenceCommandClosureResult? {
        // Leverages the authoritative observation stream to find the specific closure fact.
        // This avoids creating a duplicate cache or authority.
        return closureStream.firstOrNull { it.operationId == operationId }
    }
}
