package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Default implementation of the command closure observation boundary.
 * Maps finalized command results to their authoritative closure status.
 */
class DefaultIntelligenceCommandResultClosureObservationBoundary @Inject constructor(
    private val consumptionBoundary: IntelligenceCommandResultConsumptionBoundary,
    private val closureBoundary: IntelligenceCommandResultClosureBoundary
) : IntelligenceCommandResultClosureObservationBoundary {

    override val closureStream: Flow<IntelligenceCommandClosureResult> = 
        consumptionBoundary.resultStream.map { result ->
            closureBoundary.evaluateClosure(result)
        }
}
