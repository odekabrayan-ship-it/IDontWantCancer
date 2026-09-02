package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import kotlinx.coroutines.flow.Flow

/**
 * Authoritative boundary for ensuring that rendering contracts participate 
 * correctly in the screen lifecycle (Step 198).
 */
interface IntelligenceCommandRenderingLifecycleBoundary {
    /**
     * A stream of read-only presentation contracts for pixel rendering, 
     * governed by lifecycle-aware participation.
     */
    val lifecycleRenderingStream: Flow<CommandConsumptionFinalityPresentationContract>

    /**
     * Governs the lifecycle-aware participation of a rendering contract.
     *
     * @param request The formalized handover request from Step 198.
     */
    fun participate(
        request: IntelligenceCommandRenderingLifecycleHandoverRequest
    )
}
