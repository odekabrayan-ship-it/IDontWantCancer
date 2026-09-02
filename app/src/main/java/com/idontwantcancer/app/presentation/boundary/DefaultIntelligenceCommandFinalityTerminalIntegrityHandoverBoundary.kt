package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandFinalityTerminalIntegrityHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the finality-to-terminal-integrity bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 164).
 */
class DefaultIntelligenceCommandFinalityTerminalIntegrityHandoverBoundary @Inject constructor(
    private val integrityAuthority: IntelligenceCommandFinalityTerminalIntegrityBridgeBoundary
) : IntelligenceCommandFinalityTerminalIntegrityHandoverBoundary {

    override fun routeToIntegrity(
        finalityResult: IntelligenceCommandConsumptionFinalityResult
    ): IntelligenceCommandConsumptionFinalityResult {
        // Step 194 Logic: Formalize the terminal-integrity request from the established finality.
        val request = IntelligenceCommandFinalityTerminalIntegrityHandoverRequest(finalityResult)
        
        // Route to the authoritative gate (Step 179)
        return integrityAuthority.routeToIntegrity(request)
    }
}
