package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandUserInteractionDispatchHandoverRequest

/**
 * Authoritative boundary for ensuring that an interpreted user interaction 
 * correctly reaches the dispatch handover authority.
 */
interface IntelligenceCommandUserInteractionDispatchBridgeBoundary {
    /**
     * Routes an interpreted interaction to the dispatch handover authority.
     *
     * @param request The formalized handover request from Step 200.
     */
    fun routeToDispatchHandover(
        request: IntelligenceCommandUserInteractionDispatchHandoverRequest
    )
}
