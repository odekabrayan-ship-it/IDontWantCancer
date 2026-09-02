package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionAcknowledgementHandoverRequest

/**
 * Authoritative boundary for ensuring that a consumed result (Step 145) 
 * correctly reaches the acknowledgement authority (Step 146).
 */
interface IntelligenceCommandConsumptionAcknowledgementBoundary {
    /**
     * Routes a consumption request to the acknowledgement authority.
     *
     * @param request The formalized handover request from Step 191.
     * @return The structured acknowledgement result.
     */
    suspend fun routeToAcknowledgement(
        request: IntelligenceCommandConsumptionAcknowledgementHandoverRequest
    ): IntelligenceCommandAcknowledgementResult
}
