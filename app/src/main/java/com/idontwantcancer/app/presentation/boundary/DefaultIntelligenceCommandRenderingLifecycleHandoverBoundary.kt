package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the rendering-to-lifecycle bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 198).
 */
class DefaultIntelligenceCommandRenderingLifecycleHandoverBoundary @Inject constructor(
    private val lifecycleBridge: IntelligenceCommandRenderingLifecycleBridgeBoundary
) : IntelligenceCommandRenderingLifecycleHandoverBoundary {

    override fun routeToLifecycle(
        request: IntelligenceCommandRenderingLifecycleHandoverRequest
    ) {
        // Step 198 Logic: Route the handover request to the lifecycle participation authority.
        lifecycleBridge.routeToLifecycle(request)
    }
}
