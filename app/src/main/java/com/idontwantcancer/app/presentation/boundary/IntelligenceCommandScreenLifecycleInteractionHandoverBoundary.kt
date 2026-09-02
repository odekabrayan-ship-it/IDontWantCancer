package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandScreenLifecycleInteractionHandoverRequest

/**
 * Authoritative boundary for ensuring that an interaction originating from 
 * a lifecycle-aware screen (Step 198) correctly reaches the interaction 
 * authority (Step 199).
 */
interface IntelligenceCommandScreenLifecycleInteractionHandoverBoundary {
    /**
     * Routes a screen interaction to the interaction authority.
     *
     * @param request The formalized handover request from Step 199.
     */
    fun routeToInteraction(
        request: IntelligenceCommandScreenLifecycleInteractionHandoverRequest
    )
}
