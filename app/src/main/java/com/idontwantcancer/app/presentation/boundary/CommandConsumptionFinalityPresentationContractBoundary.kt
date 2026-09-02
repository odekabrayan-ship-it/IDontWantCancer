package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandProjectionPresentationHandoverRequest
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for ensuring that the existing presentation contract 
 * receives projected data in a read-only, UI-safe manner.
 */
interface CommandConsumptionFinalityPresentationContractBoundary {
    /**
     * Provides a read-only stream of presentation contracts for consumption finality.
     */
    val presentationStream: Flow<CommandConsumptionFinalityPresentationContract>

    /**
     * Presents a projection as a formalized contract.
     *
     * @param request The formalized presentation request from Step 196.
     */
    fun presentProjection(
        request: IntelligenceCommandProjectionPresentationHandoverRequest
    )
}
