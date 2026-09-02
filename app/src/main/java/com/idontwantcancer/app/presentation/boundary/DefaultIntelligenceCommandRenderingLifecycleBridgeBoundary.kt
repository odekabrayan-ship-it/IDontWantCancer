package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the rendering-to-lifecycle bridge.
 * Routes it to the authoritative gate (Step 198).
 */
class DefaultIntelligenceCommandRenderingLifecycleBridgeBoundary @Inject constructor(
    private val lifecycleAuthority: IntelligenceCommandRenderingLifecycleBoundary
) : IntelligenceCommandRenderingLifecycleBridgeBoundary {

    override fun routeToLifecycle(
        request: IntelligenceCommandRenderingLifecycleHandoverRequest
    ) {
        // Step 198 Logic: Pass the formalized request to the authoritative gate.
        lifecycleAuthority.participate(request)
    }
}
