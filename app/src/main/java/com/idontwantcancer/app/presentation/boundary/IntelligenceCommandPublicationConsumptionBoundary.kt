package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionRequest
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for ensuring that published results (Step 144) 
 * correctly reach the consumption authority (Step 145).
 */
interface IntelligenceCommandPublicationConsumptionBoundary {
    /**
     * Provides a stream of formalized consumption requests.
     */
    val consumptionRequestStream: Flow<IntelligenceCommandConsumptionRequest>
}
