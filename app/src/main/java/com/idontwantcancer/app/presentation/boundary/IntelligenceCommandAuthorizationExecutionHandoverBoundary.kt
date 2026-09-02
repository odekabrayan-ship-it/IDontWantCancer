package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest

/**
 * Authoritative boundary for ensuring that an authorized command (Step 170) 
 * correctly reaches the execution authority (Step 171).
 */
interface IntelligenceCommandAuthorizationExecutionHandoverBoundary {
    /**
     * Routes an authorized request to the execution authority.
     *
     * @param request The formalized dispatch request.
     * @param authorization The verified authorization result.
     */
    fun routeToExecution(
        request: IntelligenceCommandDispatchRequest,
        authorization: IntelligenceCommandAuthorizationResult
    )
}
