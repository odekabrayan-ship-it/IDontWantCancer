package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandUserInteractionDispatchHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the user-interaction-to-dispatch bridge.
 * Routes it to the authoritative gate (Step 184).
 */
class DefaultIntelligenceCommandUserInteractionDispatchBridgeBoundary @Inject constructor(
    private val dispatchHandover: IntelligenceCommandInteractionDispatchHandoverBoundary
) : IntelligenceCommandUserInteractionDispatchBridgeBoundary {

    override fun routeToDispatchHandover(
        request: IntelligenceCommandUserInteractionDispatchHandoverRequest
    ) {
        // Step 200 Logic: Pass the formalized request to the authoritative dispatch handover gate (Step 184).
        dispatchHandover.routeToDispatch(
            interaction = request.interaction,
            handler = request.handler,
            onNavigate = request.onNavigate
        )
    }
}
