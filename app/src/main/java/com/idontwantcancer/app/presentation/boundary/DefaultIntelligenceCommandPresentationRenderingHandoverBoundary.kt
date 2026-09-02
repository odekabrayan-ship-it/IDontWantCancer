package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the presentation-to-rendering bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 167).
 */
class DefaultIntelligenceCommandPresentationRenderingHandoverBoundary @Inject constructor(
    private val renderingAuthority: IntelligenceCommandPresentationRenderingBridgeBoundary
) : IntelligenceCommandPresentationRenderingHandoverBoundary {

    override fun routeToRendering(
        request: IntelligenceCommandPresentationRenderingHandoverRequest
    ) {
        // Step 197 Logic: Route the handover request to the rendering authority.
        renderingAuthority.routeToRendering(request)
    }
}
