package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the rendering-to-interaction bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 168).
 */
class DefaultIntelligenceCommandRenderingInteractionHandoverBoundary @Inject constructor(
    private val interactionAuthority: IntelligenceCommandRenderingInteractionBridgeBoundary
) : IntelligenceCommandRenderingInteractionHandoverBoundary {

    override fun routeToInteraction(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    ) {
        // Step 183 Logic: Formalize the interaction request from the rendered user action.
        val request = IntelligenceCommandRenderingInteractionHandoverRequest(interaction, handler, onNavigate)
        
        // Route to the authoritative gate (Step 168)
        interactionAuthority.routeToInteraction(request)
    }
}
