package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest

/**
 * Authoritative boundary for ensuring that a dispatch request (Step 139) 
 * correctly reaches the authorization authority (Step 140).
 */
interface IntelligenceCommandDispatchAuthorizationBoundary {
    /**
     * Routes a dispatch request to the authoritative authorization gate.
     *
     * @param request The formalized dispatch request.
     * @return The structured authorization result.
     */
    fun routeToAuthorization(
        request: IntelligenceCommandDispatchRequest
    ): IntelligenceCommandAuthorizationResult
}
