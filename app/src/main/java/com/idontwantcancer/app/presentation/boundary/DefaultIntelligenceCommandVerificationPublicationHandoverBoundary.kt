package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationPublicationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationResult
import javax.inject.Inject

/**
 * Default implementation of the verification-to-publication bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 174).
 */
class DefaultIntelligenceCommandVerificationPublicationHandoverBoundary @Inject constructor(
    private val publicationAuthority: IntelligenceCommandVerificationPublicationBoundary
) : IntelligenceCommandVerificationPublicationHandoverBoundary {

    override suspend fun routeToPublication(
        request: IntelligenceCommandVerificationPublicationHandoverRequest
    ): IntelligenceCommandPublicationResult {
        // Step 189 Logic: Route the handover request to the publication authority.
        return publicationAuthority.routeToPublication(request)
    }
}
