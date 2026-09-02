package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import javax.inject.Inject

/**
 * Default implementation of the authorization-to-execution bridge.
 * Formalizes the transition and routes to the authoritative gate (Step 156).
 */
class DefaultIntelligenceCommandAuthorizationExecutionBridgeBoundary @Inject constructor(
    private val executionAuthority: IntelligenceCommandResultClosureConsumptionFinalityExecutionBoundary
) : IntelligenceCommandAuthorizationExecutionBridgeBoundary {

    override fun routeToExecution(
        request: IntelligenceCommandAuthorizationExecutionHandoverRequest
    ) {
        // Step 171 / 186 Logic: Formalize the handover from authorization to execution.
        val handoverRequest = IntelligenceCommandAuthorizationExecutionRequest(
            dispatchRequest = request.dispatchRequest,
            authorizationResult = request.authorizationResult
        )
        
        // Ensure execution occurs ONLY if formally AUTHORIZED.
        if (handoverRequest.authorizationResult.status != IntelligenceCommandAuthorizationStatus.AUTHORIZED) {
            return
        }

        val executionRequest = IntelligenceCommandAuthorizedExecutionRequest(
            interaction = handoverRequest.dispatchRequest.interaction,
            handler = handoverRequest.dispatchRequest.handler,
            authorizationResult = handoverRequest.authorizationResult
        )

        // Route to the authoritative performer (Step 156)
        executionAuthority.execute(executionRequest)
    }
}
