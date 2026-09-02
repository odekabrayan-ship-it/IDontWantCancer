package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchAuthorizationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchAuthorizationRequest
import javax.inject.Inject

/**
 * Default implementation of the dispatch-to-authorization bridge.
 * Formalizes the transition and routes to the authoritative gate (Step 155).
 */
class DefaultIntelligenceCommandDispatchAuthorizationBridgeBoundary @Inject constructor(
    private val authorizationAuthority: IntelligenceCommandDispatchAuthorizationBoundary
) : IntelligenceCommandDispatchAuthorizationBridgeBoundary {

    override fun routeToAuthorization(
        request: IntelligenceCommandDispatchAuthorizationHandoverRequest
    ): IntelligenceCommandAuthorizationResult {
        // Step 170 / 185 Logic: Formalize the handover from dispatch to authorization.
        val authHandoverRequest = IntelligenceCommandDispatchAuthorizationRequest(request.dispatchRequest)
        
        // Route to the authoritative gate (Step 155)
        return authorizationAuthority.routeToAuthorization(authHandoverRequest.dispatchRequest)
    }
}
