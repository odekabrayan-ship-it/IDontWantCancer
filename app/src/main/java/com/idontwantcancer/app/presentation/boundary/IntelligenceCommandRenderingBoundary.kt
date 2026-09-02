package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingRequest
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for ensuring that rendering is a pure 
 * consumer of the existing presentation contract (Step 181).
 */
interface IntelligenceCommandRenderingBoundary {
    /**
     * A stream of read-only presentation contracts for pixel rendering.
     */
    val renderingStream: Flow<CommandConsumptionFinalityPresentationContract>

    /**
     * Receives a formalized rendering request.
     *
     * @param request The formalized rendering request from Step 197.
     */
    fun renderContract(
        request: IntelligenceCommandPresentationRenderingRequest
    )
}
