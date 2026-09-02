package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingHandoverRequest

/**
 * Authoritative boundary for ensuring that a presentation contract (Step 166) 
 * correctly reaches the rendering authority (Step 167).
 */
interface IntelligenceCommandPresentationRenderingHandoverBoundary {
    /**
     * Routes a presentation contract to the rendering authority.
     *
     * @param request The formalized handover request from Step 197.
     */
    fun routeToRendering(
        request: IntelligenceCommandPresentationRenderingHandoverRequest
    )
}
