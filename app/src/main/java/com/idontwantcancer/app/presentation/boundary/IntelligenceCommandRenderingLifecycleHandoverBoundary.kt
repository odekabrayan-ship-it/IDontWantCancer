package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest

/**
 * Authoritative boundary for ensuring that rendering outcomes (Step 167) 
 * correctly participate in the screen lifecycle (Step 198).
 */
interface IntelligenceCommandRenderingLifecycleHandoverBoundary {
    /**
     * Routes a rendering contract to lifecycle-aware participation.
     *
     * @param request The formalized handover request from Step 198.
     */
    fun routeToLifecycle(
        request: IntelligenceCommandRenderingLifecycleHandoverRequest
    )
}
