package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import javax.inject.Inject

/**
 * Default implementation of the dispatch-to-authorization bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 140).
 */
class DefaultIntelligenceCommandDispatchAuthorizationBoundary @Inject constructor(
    private val authorizationAuthority: IntelligenceCommandResultClosureConsumptionFinalityDispatchAuthorizationBoundary
) : IntelligenceCommandDispatchAuthorizationBoundary {

    override fun routeToAuthorization(
        request: IntelligenceCommandDispatchRequest
    ): IntelligenceCommandAuthorizationResult {
        // Step 155 Logic: Formalize the authorization request
        val authRequest = IntelligenceCommandAuthorizationRequest(
            interaction = request.interaction,
            commandIdentity = request.interaction.hashCode().toString() // Identical mapping to dispatcher logic
        )
        
        // Route to the authoritative gate (Step 140)
        return authorizationAuthority.evaluateAuthorization(authRequest)
    }
}
