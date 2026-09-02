package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that an interpreted interaction (Step 138) 
 * correctly reaches the dispatch authority (Step 139).
 */
interface IntelligenceCommandInteractionDispatchBoundary {
    /**
     * Translates an interpreted interaction into a dispatch request and 
     * routes it to the dispatcher.
     *
     * @param request The formalized dispatch request from Step 169.
     */
    fun dispatchInteraction(
        request: IntelligenceCommandDispatchRequest
    )
}
