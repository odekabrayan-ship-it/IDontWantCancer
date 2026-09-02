package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the terminal-integrity-to-projection bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 180).
 */
class DefaultIntelligenceCommandTerminalIntegrityProjectionHandoverBoundary @Inject constructor(
    private val projectionAuthority: IntelligenceCommandTerminalIntegrityProjectionBridgeBoundary
) : IntelligenceCommandTerminalIntegrityProjectionHandoverBoundary {

    override fun routeToProjection(
        request: IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
    ) {
        // Step 195 Logic: Route the handover request to the projection authority.
        projectionAuthority.routeToProjection(request)
    }
}
