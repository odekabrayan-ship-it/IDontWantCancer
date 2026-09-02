package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandProjectionPresentationHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the projection-to-presentation bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 181).
 */
class DefaultIntelligenceCommandProjectionPresentationBridgeBoundary @Inject constructor(
    private val presentationAuthority: CommandConsumptionFinalityPresentationContractBoundary
) : IntelligenceCommandProjectionPresentationBridgeBoundary {

    override fun routeToPresentation(
        request: IntelligenceCommandProjectionPresentationHandoverRequest
    ) {
        // Step 166 / 181 / 196 Logic: Pass the formalized request to the authoritative gate.
        presentationAuthority.presentProjection(request)
    }
}
