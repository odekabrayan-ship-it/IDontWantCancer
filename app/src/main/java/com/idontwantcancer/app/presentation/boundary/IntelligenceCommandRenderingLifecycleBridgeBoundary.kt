package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest

/**
 * Authoritative boundary for ensuring that a rendering outcome (Step 167) 
 * correctly reaches the lifecycle participation authority (Step 198).
 */
interface IntelligenceCommandRenderingLifecycleBridgeBoundary {
    /**
     * Routes a rendering contract to the lifecycle authority.
     *
     * @param request The formalized handover request from Step 198.
     */
    fun routeToLifecycle(
        request: IntelligenceCommandRenderingLifecycleHandoverRequest
    )
}
