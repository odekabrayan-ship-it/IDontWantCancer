package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultClosureConsumptionAcknowledgementResult

/**
 * Authoritative boundary through which downstream consumers acknowledge 
 * receiving and processing authoritative closure information.
 */
interface IntelligenceCommandResultClosureConsumptionAcknowledgementBoundary {
    /**
     * Records an acknowledgement for a consumed closure result.
     *
     * @param closure The closure fact that was consumed.
     * @return The structured acknowledgement result.
     */
    suspend fun acknowledgeConsumption(
        closure: IntelligenceCommandClosureResult
    ): IntelligenceCommandResultClosureConsumptionAcknowledgementResult
}
