package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the terminal-integrity-to-projection bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 180).
 */
class DefaultIntelligenceCommandTerminalIntegrityProjectionBridgeBoundary @Inject constructor(
    private val projectionAuthority: CommandConsumptionFinalityProjectionBoundary
) : IntelligenceCommandTerminalIntegrityProjectionBridgeBoundary {

    override fun routeToProjection(
        request: IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
    ) {
        // Step 165 / 180 / 195 Logic: Route the handover request to the projection authority.
        projectionAuthority.projectTerminality(request)
    }
}
