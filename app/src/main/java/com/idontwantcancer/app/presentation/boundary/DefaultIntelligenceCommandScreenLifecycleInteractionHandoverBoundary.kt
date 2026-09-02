package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandScreenLifecycleInteractionHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the screen-lifecycle-to-interaction bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 199).
 */
class DefaultIntelligenceCommandScreenLifecycleInteractionHandoverBoundary @Inject constructor(
    private val interactionBridge: IntelligenceCommandScreenLifecycleInteractionBridgeBoundary
) : IntelligenceCommandScreenLifecycleInteractionHandoverBoundary {

    override fun routeToInteraction(
        request: IntelligenceCommandScreenLifecycleInteractionHandoverRequest
    ) {
        // Step 199 Logic: Route the handover request to the interaction bridge.
        interactionBridge.routeToInteraction(request)
    }
}
