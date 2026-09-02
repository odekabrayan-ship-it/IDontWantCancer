package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureFinalityHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import javax.inject.Inject

/**
 * Default implementation of the closure-to-finality bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 178).
 */
class DefaultIntelligenceCommandClosureFinalityHandoverBoundary @Inject constructor(
    private val finalityAuthority: IntelligenceCommandClosureFinalityBridgeBoundary
) : IntelligenceCommandClosureFinalityHandoverBoundary {

    override fun routeToFinality(
        request: IntelligenceCommandClosureFinalityHandoverRequest
    ): IntelligenceCommandConsumptionFinalityResult {
        // Step 193 Logic: Route the handover request to the finality authority.
        return finalityAuthority.routeToFinality(request)
    }
}
