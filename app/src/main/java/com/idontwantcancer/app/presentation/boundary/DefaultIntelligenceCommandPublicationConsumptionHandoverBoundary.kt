package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationConsumptionHandoverRequest
import javax.inject.Inject

/**
 * Default implementation of the publication-to-consumption bridge.
 * Formalizes the transition and maps results to consumption requests.
 */
class DefaultIntelligenceCommandPublicationConsumptionHandoverBoundary @Inject constructor() : 
    IntelligenceCommandPublicationConsumptionHandoverBoundary {

    override fun routeToConsumption(
        request: IntelligenceCommandPublicationConsumptionHandoverRequest
    ): IntelligenceCommandConsumptionRequest {
        // Step 190 Logic: Formalize the consumption request from the published result.
        return IntelligenceCommandConsumptionRequest(
            result = request.result,
            publicationResult = request.publicationResult
        )
    }
}
