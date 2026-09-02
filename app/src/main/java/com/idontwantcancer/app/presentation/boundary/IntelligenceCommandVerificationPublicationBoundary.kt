package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandVerificationPublicationHandoverRequest

/**
 * Authoritative boundary for ensuring that a verified result (Step 143) 
 * correctly reaches the publication authority (Step 144).
 */
interface IntelligenceCommandVerificationPublicationBoundary {
    /**
     * Routes a verified result to the publication authority.
     *
     * @param request The formalized handover request from Step 189.
     * @return The structured publication result.
     */
    suspend fun routeToPublication(
        request: IntelligenceCommandVerificationPublicationHandoverRequest
    ): IntelligenceCommandPublicationResult
}
