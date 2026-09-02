package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureFinalityHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandFinalityRequest
import javax.inject.Inject

/**
 * Default implementation of the closure-to-finality bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 148).
 */
class DefaultIntelligenceCommandClosureFinalityBridgeBoundary @Inject constructor(
    private val finalityAuthority: IntelligenceCommandResultClosureFinalityBoundary
) : IntelligenceCommandClosureFinalityBridgeBoundary {

    override fun routeToFinality(
        request: IntelligenceCommandClosureFinalityHandoverRequest
    ): IntelligenceCommandConsumptionFinalityResult {
        // Step 163 / 178 / 193 Logic: Formalize the finality request
        val finalityRequest = IntelligenceCommandFinalityRequest(request.closure)
        
        // Route to the authoritative gate (Step 148)
        return finalityAuthority.evaluateFinality(finalityRequest)
    }
}
