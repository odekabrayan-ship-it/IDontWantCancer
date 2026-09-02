package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandUserInteractionDispatchHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the user-interaction-to-dispatch bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 184).
 */
class DefaultIntelligenceCommandUserInteractionDispatchHandoverBoundary @Inject constructor(
    private val interactionBridge: IntelligenceCommandUserInteractionDispatchBridgeBoundary
) : IntelligenceCommandUserInteractionDispatchHandoverBoundary {

    override fun routeToDispatchHandover(
        request: IntelligenceCommandUserInteractionDispatchHandoverRequest
    ) {
        // Step 200 Logic: Route the handover request to the dispatch interaction bridge.
        interactionBridge.routeToDispatchHandover(request)
    }
}
