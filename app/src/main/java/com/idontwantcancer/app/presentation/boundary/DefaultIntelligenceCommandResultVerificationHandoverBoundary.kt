package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultVerificationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationResult
import javax.inject.Inject

/**
 * Default implementation of the result-to-verification bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 158).
 */
class DefaultIntelligenceCommandResultVerificationHandoverBoundary @Inject constructor(
    private val verificationAuthority: IntelligenceCommandResultVerificationBridgeBoundary
) : IntelligenceCommandResultVerificationHandoverBoundary {

    override suspend fun routeToVerification(
        request: IntelligenceCommandResultVerificationHandoverRequest
    ): IntelligenceCommandVerificationResult {
        // Step 188 Logic: Route the handover request to the verification authority.
        return verificationAuthority.routeToVerification(request)
    }
}
