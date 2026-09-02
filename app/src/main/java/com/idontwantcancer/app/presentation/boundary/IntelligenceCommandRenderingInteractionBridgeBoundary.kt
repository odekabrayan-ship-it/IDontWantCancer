package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionHandoverRequest

/**
 * Authoritative boundary for ensuring that a rendered user action (Step 152) 
 * correctly reaches the interaction authority (Step 153).
 */
interface IntelligenceCommandRenderingInteractionBridgeBoundary {
    /**
     * Routes a rendered user action to the interaction authority.
     *
     * @param request The formalized handover request from Step 183.
     */
    fun routeToInteraction(
        request: IntelligenceCommandRenderingInteractionHandoverRequest
    )
}
