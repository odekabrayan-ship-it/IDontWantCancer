package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationResult

/**
 * Authoritative boundary for establishing the verification outcome 
 * of a command result.
 */
interface IntelligenceCommandResultVerificationBoundary {
    /**
     * Verifies an authoritative command result.
     *
     * @param request The formalized verification request from Step 173.
     * @return The structured verification result.
     */
    suspend fun verifyResult(
        request: IntelligenceCommandResultVerificationRequest
    ): IntelligenceCommandVerificationResult
}
