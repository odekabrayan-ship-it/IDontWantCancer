package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcknowledgementResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionAcknowledgementHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionRequest
import javax.inject.Inject

/**
 * Default implementation of the consumption-to-acknowledgement bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 161).
 */
class DefaultIntelligenceCommandConsumptionAcknowledgementHandoverBoundary @Inject constructor(
    private val acknowledgementAuthority: IntelligenceCommandConsumptionAcknowledgementBoundary
) : IntelligenceCommandConsumptionAcknowledgementHandoverBoundary {

    override suspend fun routeToAcknowledgement(
        request: IntelligenceCommandConsumptionAcknowledgementHandoverRequest
    ): IntelligenceCommandAcknowledgementResult {
        // Step 191 Logic: Route the handover request to the acknowledgement authority.
        return acknowledgementAuthority.routeToAcknowledgement(request)
    }
}
