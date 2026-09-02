package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationExecutionHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import javax.inject.Inject

/**
 * Default implementation of the authorization-to-execution bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 171).
 */
class DefaultIntelligenceCommandAuthorizationExecutionHandoverBoundary @Inject constructor(
    private val executionAuthority: IntelligenceCommandAuthorizationExecutionBridgeBoundary
) : IntelligenceCommandAuthorizationExecutionHandoverBoundary {

    override fun routeToExecution(
        request: IntelligenceCommandDispatchRequest,
        authorization: IntelligenceCommandAuthorizationResult
    ) {
        // Step 186 Logic: Formalize the execution request from the authorization decision.
        val handoverRequest = IntelligenceCommandAuthorizationExecutionHandoverRequest(request, authorization)
        
        // Route to the authoritative gate (Step 171)
        executionAuthority.routeToExecution(handoverRequest)
    }
}
