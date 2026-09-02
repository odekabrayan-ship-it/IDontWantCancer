package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionAcknowledgementHandoverRequest

/**
 * Authoritative boundary for ensuring that a consumed result (Step 160) 
 * correctly reaches the acknowledgement authority (Step 161).
 */
interface IntelligenceCommandConsumptionAcknowledgementHandoverBoundary {
    /**
     * Routes a consumed result to the acknowledgement authority.
     *
     * @param request The formalized handover request from Step 191.
     * @return The structured acknowledgement result.
     */
    suspend fun routeToAcknowledgement(
        request: IntelligenceCommandConsumptionAcknowledgementHandoverRequest
    ): IntelligenceCommandAcknowledgementResult
}
