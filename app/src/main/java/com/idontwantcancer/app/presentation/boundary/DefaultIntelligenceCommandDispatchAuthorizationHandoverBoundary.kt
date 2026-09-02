package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchAuthorizationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import javax.inject.Inject

/**
 * Default implementation of the dispatch-to-authorization bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 170).
 */
class DefaultIntelligenceCommandDispatchAuthorizationHandoverBoundary @Inject constructor(
    private val authorizationAuthority: IntelligenceCommandDispatchAuthorizationBridgeBoundary
) : IntelligenceCommandDispatchAuthorizationHandoverBoundary {

    override fun routeToAuthorization(
        request: IntelligenceCommandDispatchRequest
    ): IntelligenceCommandAuthorizationResult {
        // Step 185 Logic: Formalize the authorization request from the dispatch request.
        val handoverRequest = IntelligenceCommandDispatchAuthorizationHandoverRequest(request)
        
        // Route to the authoritative gate (Step 170)
        return authorizationAuthority.routeToAuthorization(handoverRequest)
    }
}
