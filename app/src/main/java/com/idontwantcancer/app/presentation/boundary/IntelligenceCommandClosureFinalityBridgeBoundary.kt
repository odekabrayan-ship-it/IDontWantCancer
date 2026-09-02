package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureFinalityHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult

/**
 * Authoritative boundary for ensuring that a closed result (Step 147) 
 * correctly reaches the finality authority (Step 148).
 */
interface IntelligenceCommandClosureFinalityBridgeBoundary {
    /**
     * Routes a closure result to the finality authority.
     *
     * @param request The formalized handover request from Step 193.
     * @return The structured finality result.
     */
    fun routeToFinality(
        request: IntelligenceCommandClosureFinalityHandoverRequest
    ): IntelligenceCommandConsumptionFinalityResult
}
