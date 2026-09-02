package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionRequest
import javax.inject.Inject

/**
 * Default implementation of the rendering-to-interaction bridge.
 * Formalizes the user action and routes it to the authoritative gate (Step 153).
 */
class DefaultIntelligenceCommandRenderingInteractionBridgeBoundary @Inject constructor(
    private val interactionAuthority: IntelligenceCommandInteractionAuthority
) : IntelligenceCommandRenderingInteractionBridgeBoundary {

    override fun routeToInteraction(
        request: IntelligenceCommandRenderingInteractionHandoverRequest
    ) {
        // Step 168 / 183 Logic: Formalize the interaction request
        val interactionRequest = IntelligenceCommandRenderingInteractionRequest(
            interaction = request.interaction,
            handler = request.handler,
            onNavigate = request.onNavigate
        )
        
        // Route to the authoritative gate (Step 153)
        interactionAuthority.handleInteraction(interactionRequest)
    }
}
