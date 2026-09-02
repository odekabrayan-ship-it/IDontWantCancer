package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementClosureHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult

/**
 * Authoritative boundary for ensuring that an acknowledged result (Step 161) 
 * correctly reaches the closure authority (Step 162).
 */
interface IntelligenceCommandAcknowledgementClosureHandoverBoundary {
    /**
     * Routes an acknowledged result to the closure authority.
     *
     * @param request The formalized handover request from Step 192.
     * @return The structured closure result.
     */
    suspend fun routeToClosure(
        request: IntelligenceCommandAcknowledgementClosureHandoverRequest
    ): IntelligenceCommandClosureResult
}
