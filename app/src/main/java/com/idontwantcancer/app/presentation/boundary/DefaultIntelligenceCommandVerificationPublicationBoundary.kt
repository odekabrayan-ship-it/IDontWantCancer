package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationPublicationHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the verification-to-publication bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 144).
 */
class DefaultIntelligenceCommandVerificationPublicationBoundary @Inject constructor(
    private val publicationAuthority: IntelligenceCommandResultPublicationBoundary
) : IntelligenceCommandVerificationPublicationBoundary {

    override suspend fun routeToPublication(
        request: IntelligenceCommandVerificationPublicationHandoverRequest
    ): IntelligenceCommandPublicationResult {
        // Step 159 / 189 Logic: Formalize the publication request
        val publicationRequest = IntelligenceCommandPublicationRequest(
            result = request.result,
            verificationResult = request.verificationResult
        )
        
        // Route to the authoritative gate (Step 144)
        return publicationAuthority.publishResult(publicationRequest)
    }
}
