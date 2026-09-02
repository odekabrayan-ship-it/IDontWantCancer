package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchAuthorizationHandoverRequest

/**
 * Authoritative boundary for ensuring that a dispatch request (Step 154) 
 * correctly reaches the authorization authority (Step 155).
 */
interface IntelligenceCommandDispatchAuthorizationBridgeBoundary {
    /**
     * Routes a dispatch request to the authorization authority.
     *
     * @param request The formalized handover request from Step 185.
     * @return The structured authorization result.
     */
    fun routeToAuthorization(
        request: IntelligenceCommandDispatchAuthorizationHandoverRequest
    ): IntelligenceCommandAuthorizationResult
}
