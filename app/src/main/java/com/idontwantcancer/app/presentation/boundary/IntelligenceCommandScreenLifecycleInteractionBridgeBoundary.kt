package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandScreenLifecycleInteractionHandoverRequest

/**
 * Authoritative boundary for ensuring that an interaction from a lifecycle-aware 
 * screen correctly reaches the authoritative interaction gate.
 */
interface IntelligenceCommandScreenLifecycleInteractionBridgeBoundary {
    /**
     * Routes a screen interaction to the authoritative interaction authority.
     *
     * @param request The formalized handover request from Step 199.
     */
    fun routeToInteraction(
        request: IntelligenceCommandScreenLifecycleInteractionHandoverRequest
    )
}
