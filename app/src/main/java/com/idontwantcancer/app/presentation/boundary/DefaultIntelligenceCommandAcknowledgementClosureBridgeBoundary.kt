package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementClosureHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import javax.inject.Inject

/**
 * Default implementation of the acknowledgement-to-closure bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 147).
 */
class DefaultIntelligenceCommandAcknowledgementClosureBridgeBoundary @Inject constructor(
    private val closureAuthority: IntelligenceCommandResultAcknowledgementClosureBoundary
) : IntelligenceCommandAcknowledgementClosureBridgeBoundary {

    override suspend fun routeToClosure(
        request: IntelligenceCommandAcknowledgementClosureHandoverRequest
    ): IntelligenceCommandClosureResult {
        // Step 162 / 177 / 192 Logic: Pass the formalized request to the authoritative gate.
        return closureAuthority.evaluateClosure(request)
    }
}
