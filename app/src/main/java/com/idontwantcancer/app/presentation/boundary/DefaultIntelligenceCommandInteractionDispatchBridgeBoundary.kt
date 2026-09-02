package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandInteractionDispatchHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispatchRequest
import javax.inject.Inject

/**
 * Default implementation of the interaction-to-dispatch bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 154).
 */
class DefaultIntelligenceCommandInteractionDispatchBridgeBoundary @Inject constructor(
    private val dispatchAuthority: IntelligenceCommandInteractionDispatchBoundary
) : IntelligenceCommandInteractionDispatchBridgeBoundary {

    override fun routeToDispatch(
        request: IntelligenceCommandInteractionDispatchHandoverRequest
    ) {
        // Step 169 / 184 Logic: Formalize the dispatch request
        val dispatchRequest = IntelligenceCommandDispatchRequest(
            interaction = request.interaction,
            handler = request.handler,
            onNavigate = request.onNavigate
        )
        
        // Route to the authoritative gate (Step 154)
        dispatchAuthority.dispatchInteraction(dispatchRequest)
    }
}
