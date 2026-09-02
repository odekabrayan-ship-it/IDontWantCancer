package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationHandoverRequest

/**
 * Authoritative boundary for ensuring that an established result (Step 157) 
 * correctly reaches the verification authority (Step 158).
 */
interface IntelligenceCommandResultVerificationHandoverBoundary {
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
