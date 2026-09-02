package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationExecutionHandoverRequest

/**
 * Authoritative boundary for ensuring that an authorized command request (Step 155) 
 * correctly reaches the execution authority (Step 156).
 */
interface IntelligenceCommandAuthorizationExecutionBridgeBoundary {
    /**
     * Routes an authorized request to the execution authority.
     *
     * @param request The formalized handover request from Step 186.
     */
    fun routeToExecution(
        request: IntelligenceCommandAuthorizationExecutionHandoverRequest
    )
}
