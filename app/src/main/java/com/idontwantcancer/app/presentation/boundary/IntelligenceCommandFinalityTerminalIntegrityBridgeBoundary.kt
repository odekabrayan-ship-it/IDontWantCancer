package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandFinalityTerminalIntegrityHandoverRequest

/**
 * Authoritative boundary for ensuring that an established finality result (Step 148) 
 * correctly reaches the terminal-state integrity authority (Step 149).
 */
interface IntelligenceCommandFinalityTerminalIntegrityBridgeBoundary {
    /**
     * Routes an established finality result to the terminal-integrity authority.
     *
     * @param request The formalized handover request from Step 194.
     * @return The verified/protected finality result.
     */
    fun routeToIntegrity(
        request: IntelligenceCommandFinalityTerminalIntegrityHandoverRequest
    ): IntelligenceCommandConsumptionFinalityResult
}
