package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationResult

/**
 * Authoritative boundary for ensuring that an established result (Step 142) 
 * correctly reaches the verification authority (Step 143).
 */
interface IntelligenceCommandResultVerificationBridgeBoundary {
    /**
     * Routes an established result to the verification authority.
     *
     * @param request The formalized handover request from Step 188.
     * @return The structured verification result.
     */
    suspend fun routeToVerification(
        request: IntelligenceCommandResultVerificationHandoverRequest
    ): IntelligenceCommandVerificationResult
}
