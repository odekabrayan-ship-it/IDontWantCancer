package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPublicationResult
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for ensuring that verified command results 
 * enter the existing publication pipeline.
 */
interface IntelligenceCommandResultPublicationBoundary {
    /**
     * Publishes a verified command result to authorized destinations.
     *
     * @param request The formalized publication request from Step 159.
     * @return The structured publication outcome.
     */
    suspend fun publishResult(
        request: IntelligenceCommandPublicationRequest
    ): IntelligenceCommandPublicationResult

    /**
     * A stream of formalized consumption requests derived from successful publications.
     */
    val consumptionStream: Flow<IntelligenceCommandConsumptionRequest>
}
