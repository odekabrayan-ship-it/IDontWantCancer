package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementClosureHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult

/**
 * Authoritative boundary for ensuring that an acknowledged result correctly 
 * enters the closure authority.
 */
interface IntelligenceCommandResultAcknowledgementClosureBoundary {
    /**
     * Evaluates closure for an acknowledged command result.
     *
     * @param request The formalized closure request from Step 192.
     * @return The structured closure result (Step 128 product).
     */
    suspend fun evaluateClosure(
        request: IntelligenceCommandAcknowledgementClosureHandoverRequest
    ): IntelligenceCommandClosureResult
}
