package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingInteractionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandUserInteractionDispatchHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the interaction authority.
 * Acts as the entry point for the command reporting loop (Step 153).
 */
class DefaultIntelligenceCommandInteractionAuthority @Inject constructor(
    private val dispatchHandoverBridge: IntelligenceCommandUserInteractionDispatchHandoverBoundary
) : IntelligenceCommandInteractionAuthority {

    override fun handleInteraction(
        request: IntelligenceCommandRenderingInteractionRequest
    ) {
        // Step 153 / 169 / 184 / 199 / 200 Logic: Formalize and route to the dispatch handover bridge.
        val handoverRequest = IntelligenceCommandUserInteractionDispatchHandoverRequest(
            interaction = request.interaction,
            handler = request.handler,
            onNavigate = request.onNavigate
        )
        dispatchHandoverBridge.routeToDispatchHandover(handoverRequest)
    }
}
