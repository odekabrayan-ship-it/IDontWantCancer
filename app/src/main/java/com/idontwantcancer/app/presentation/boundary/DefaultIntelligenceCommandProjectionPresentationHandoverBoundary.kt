package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandProjectionPresentationHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the projection-to-presentation bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 181).
 */
class DefaultIntelligenceCommandProjectionPresentationHandoverBoundary @Inject constructor(
    private val presentationAuthority: IntelligenceCommandProjectionPresentationBridgeBoundary
) : IntelligenceCommandProjectionPresentationHandoverBoundary {

    override fun routeToPresentation(
        request: IntelligenceCommandProjectionPresentationHandoverRequest
    ) {
        // Step 196 Logic: Route the handover request to the presentation authority.
        presentationAuthority.routeToPresentation(request)
    }
}
