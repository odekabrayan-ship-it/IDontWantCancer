package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandProjectionPresentationHandoverRequest

/**
 * Authoritative boundary for ensuring that a projection (Step 180) 
 * correctly reaches the presentation contract authority (Step 181).
 */
interface IntelligenceCommandProjectionPresentationBridgeBoundary {
    /**
     * Routes a projection to the presentation authority.
     *
     * @param request The formalized handover request from Step 196.
     */
    fun routeToPresentation(
        request: IntelligenceCommandProjectionPresentationHandoverRequest
    )
}
