package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandFinalityTerminalIntegrityHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityRequest
import javax.inject.Inject

/**
 * Default implementation of the finality-to-terminal-integrity bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 149).
 */
class DefaultIntelligenceCommandFinalityTerminalIntegrityBridgeBoundary @Inject constructor(
    private val integrityAuthority: IntelligenceCommandTerminalStateIntegrityBoundary
) : IntelligenceCommandFinalityTerminalIntegrityBridgeBoundary {

    override fun routeToIntegrity(
        request: IntelligenceCommandFinalityTerminalIntegrityHandoverRequest
    ): IntelligenceCommandConsumptionFinalityResult {
        // Step 179 / 194 Logic: Formalize the terminal-integrity request
        val integrityRequest = IntelligenceCommandTerminalIntegrityRequest(request.finalityResult)
        
        // Route to the authoritative gate (Step 164)
        return integrityAuthority.protectFinality(integrityRequest)
    }
}
