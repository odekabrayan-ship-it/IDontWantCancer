package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandScreenLifecycleInteractionHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the screen-lifecycle-to-interaction bridge.
 * Routes it to the authoritative gate (Step 199).
 */
class DefaultIntelligenceCommandScreenLifecycleInteractionBridgeBoundary @Inject constructor(
    private val interactionAuthority: IntelligenceCommandInteractionAuthority
) : IntelligenceCommandScreenLifecycleInteractionBridgeBoundary {

    override fun routeToInteraction(
        request: IntelligenceCommandScreenLifecycleInteractionHandoverRequest
    ) {
        // Step 199 Logic: Formalize the rendering interaction request
        val interactionRequest = IntelligenceCommandRenderingInteractionRequest(
            interaction = request.interaction,
            handler = request.handler,
            onNavigate = request.onNavigate
        )
        
        // Route to the authoritative gate (Step 168 / 184)
        interactionAuthority.handleInteraction(interactionRequest)
    }
}
