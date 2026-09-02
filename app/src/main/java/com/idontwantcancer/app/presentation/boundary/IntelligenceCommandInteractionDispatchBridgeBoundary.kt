package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandInteractionDispatchHandoverRequest

/**
 * Authoritative boundary for ensuring that an interpreted interaction (Step 153) 
 * correctly reaches the dispatch authority (Step 154).
 */
interface IntelligenceCommandInteractionDispatchBridgeBoundary {
    /**
     * Routes an interpreted interaction to the dispatch authority.
     *
     * @param request The formalized handover request from Step 184.
     */
    fun routeToDispatch(
        request: IntelligenceCommandInteractionDispatchHandoverRequest
    )
}
