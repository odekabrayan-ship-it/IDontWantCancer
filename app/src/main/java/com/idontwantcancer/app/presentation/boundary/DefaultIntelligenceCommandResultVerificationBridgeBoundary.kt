package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationResult
import javax.inject.Inject

/**
 * Default implementation of the result-to-verification bridge.
 * Formalizes the result and routes it to the authoritative gate (Step 143).
 */
class DefaultIntelligenceCommandResultVerificationBridgeBoundary @Inject constructor(
    private val verificationAuthority: IntelligenceCommandResultVerificationBoundary
) : IntelligenceCommandResultVerificationBridgeBoundary {

    override suspend fun routeToVerification(
        request: IntelligenceCommandResultVerificationHandoverRequest
    ): IntelligenceCommandVerificationResult {
        // Step 173 / 188 Logic: Formalize the verification request
        val verificationRequest = IntelligenceCommandResultVerificationRequest(request.result)
        
        // Route to the authoritative gate (Step 143/158)
        return verificationAuthority.verifyResult(verificationRequest)
    }
}
