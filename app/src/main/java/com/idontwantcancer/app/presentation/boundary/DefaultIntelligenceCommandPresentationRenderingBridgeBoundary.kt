package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingRequest
import javax.inject.Inject

/**
 * Default implementation of the presentation-to-rendering bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 197).
 */
class DefaultIntelligenceCommandPresentationRenderingBridgeBoundary @Inject constructor(
    private val renderingAuthority: IntelligenceCommandRenderingBoundary
) : IntelligenceCommandPresentationRenderingBridgeBoundary {

    override fun routeToRendering(
        request: IntelligenceCommandPresentationRenderingHandoverRequest
    ) {
        // Step 167 / 182 / 197 Logic: Formalize the rendering request
        val renderingRequest = IntelligenceCommandPresentationRenderingRequest(request.contract)
        
        // Route to the authoritative gate (Step 167)
        renderingAuthority.renderContract(renderingRequest)
    }
}
