package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationConsumptionHandoverRequest

/**
 * Authoritative boundary for ensuring that a published result (Step 159) 
 * correctly reaches the consumption authority (Step 160).
 */
interface IntelligenceCommandPublicationConsumptionHandoverBoundary {
    /**
     * Routes a published result to the consumption authority.
     *
     * @param request The formalized handover request from Step 190.
     * @return The structured consumption request.
     */
    fun routeToConsumption(
        request: IntelligenceCommandPublicationConsumptionHandoverRequest
    ): IntelligenceCommandConsumptionRequest
}
