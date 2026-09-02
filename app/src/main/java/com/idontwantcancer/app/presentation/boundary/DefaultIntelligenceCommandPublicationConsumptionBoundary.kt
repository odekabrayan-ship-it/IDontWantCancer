package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Default implementation of the publication-to-consumption bridge.
 * Passes formalized requests from Step 144 to the consumption authority (Step 145).
 */
class DefaultIntelligenceCommandPublicationConsumptionBoundary @Inject constructor(
    private val publicationAuthority: IntelligenceCommandResultPublicationBoundary
) : IntelligenceCommandPublicationConsumptionBoundary {

    override val consumptionRequestStream: Flow<IntelligenceCommandConsumptionRequest> = 
        publicationAuthority.consumptionStream
}
