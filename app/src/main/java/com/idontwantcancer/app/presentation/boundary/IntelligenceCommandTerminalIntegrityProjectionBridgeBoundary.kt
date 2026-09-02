package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityProjectionHandoverRequest

/**
 * Authoritative boundary for ensuring that a validated terminal state (Step 179) 
 * correctly reaches the projection authority (Step 180).
 */
interface IntelligenceCommandTerminalIntegrityProjectionBridgeBoundary {
    /**
     * Routes a validated terminal state to the projection authority.
     *
     * @param request The formalized handover request from Step 195.
     */
    fun routeToProjection(
        request: IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
    )
}
