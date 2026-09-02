package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandInteractionDispatchHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the interaction-to-dispatch bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 169).
 */
class DefaultIntelligenceCommandInteractionDispatchHandoverBoundary @Inject constructor(
    private val dispatchAuthority: IntelligenceCommandInteractionDispatchBridgeBoundary
) : IntelligenceCommandInteractionDispatchHandoverBoundary {

    override fun routeToDispatch(
        interaction: IntelligenceUiInteraction,
        handler: IntelligenceInteractionBoundary,
        onNavigate: (Any) -> Unit
    ) {
        // Step 184 Logic: Formalize the dispatch request from the interpreted interaction.
        val request = IntelligenceCommandInteractionDispatchHandoverRequest(interaction, handler, onNavigate)
        
        // Route to the authoritative gate (Step 169)
        dispatchAuthority.routeToDispatch(request)
    }
}
