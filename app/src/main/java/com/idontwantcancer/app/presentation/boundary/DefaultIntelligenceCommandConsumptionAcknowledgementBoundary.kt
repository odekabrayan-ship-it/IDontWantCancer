package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionAcknowledgementHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the consumption-to-acknowledgement bridge.
 * Formalizes the request and routes it to the authoritative gate (Step 146).
 */
class DefaultIntelligenceCommandConsumptionAcknowledgementBoundary @Inject constructor(
    private val acknowledgementAuthority: IntelligenceCommandResultAcknowledgementBoundary
) : IntelligenceCommandConsumptionAcknowledgementBoundary {

    override suspend fun routeToAcknowledgement(
        request: IntelligenceCommandConsumptionAcknowledgementHandoverRequest
    ): IntelligenceCommandAcknowledgementResult {
        // Step 161 / 191 Logic: Formalize the acknowledgement request
        val ackRequest = IntelligenceCommandAcknowledgementRequest(request.result)
        
        // Route to the authoritative gate (Step 146)
        return acknowledgementAuthority.acknowledge(ackRequest)
    }
}
