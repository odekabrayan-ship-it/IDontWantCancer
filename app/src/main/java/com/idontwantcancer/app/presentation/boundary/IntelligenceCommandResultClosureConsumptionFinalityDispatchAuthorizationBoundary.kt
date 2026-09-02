package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAuthorizationResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that an explicitly dispatched action 
 * cannot bypass the existing authorization boundary.
 */
interface IntelligenceCommandResultClosureConsumptionFinalityDispatchAuthorizationBoundary {
    /**
     * Evaluates whether a dispatch request is permitted to proceed to execution.
     *
     * @param request The formal authorization request from Step 155.
     * @return The structured authorization result.
     */
    fun evaluateAuthorization(
        request: IntelligenceCommandAuthorizationRequest
    ): IntelligenceCommandAuthorizationResult
}
