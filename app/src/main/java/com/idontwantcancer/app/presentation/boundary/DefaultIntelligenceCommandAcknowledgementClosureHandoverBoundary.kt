package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementClosureHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import javax.inject.Inject

/**
 * Default implementation of the acknowledgement-to-closure bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 162).
 */
class DefaultIntelligenceCommandAcknowledgementClosureHandoverBoundary @Inject constructor(
    private val closureAuthority: IntelligenceCommandAcknowledgementClosureBridgeBoundary
) : IntelligenceCommandAcknowledgementClosureHandoverBoundary {

    override suspend fun routeToClosure(
        request: IntelligenceCommandAcknowledgementClosureHandoverRequest
    ): IntelligenceCommandClosureResult {
        // Step 192 Logic: Route the handover request to the closure authority.
        return closureAuthority.routeToClosure(request)
    }
}
