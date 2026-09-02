package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultClosureConsumptionAcknowledgementResult
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the closure consumption acknowledgement boundary.
 * Provides a deterministic registration point for downstream consumers to 
 * confirm terminal processing.
 */
class DefaultIntelligenceCommandResultClosureConsumptionAcknowledgementBoundary @Inject constructor() : 
    IntelligenceCommandResultClosureConsumptionAcknowledgementBoundary {

    override suspend fun acknowledgeConsumption(
        closure: IntelligenceCommandClosureResult
    ): IntelligenceCommandResultClosureConsumptionAcknowledgementResult {
        // Deterministic acknowledgement.
        // As per Step 131, this recognizes that the consumer processed the CLOSED state.
        // It does not redefine the command result or domain truth.
        return IntelligenceCommandResultClosureConsumptionAcknowledgementResult(
            operationId = closure.operationId,
            acknowledgedAt = Instant.now(),
            reason = "Closure observation consumed and acknowledged by downstream authority."
        )
    }
}
