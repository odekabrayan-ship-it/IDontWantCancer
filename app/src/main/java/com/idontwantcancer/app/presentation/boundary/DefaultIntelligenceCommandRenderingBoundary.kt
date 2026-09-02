package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the rendering authority.
 * Acts as the final source of state for UI pixels (Step 152).
 */
@Singleton
class DefaultIntelligenceCommandRenderingBoundary @Inject constructor(
    private val lifecycleHandoverBoundary: IntelligenceCommandRenderingLifecycleHandoverBoundary
) : IntelligenceCommandRenderingBoundary {

    private val _renderingStream = MutableSharedFlow<CommandConsumptionFinalityPresentationContract>(replay = 1)
    override val renderingStream: Flow<CommandConsumptionFinalityPresentationContract> = 
        _renderingStream.asSharedFlow()

    override fun renderContract(
        request: IntelligenceCommandPresentationRenderingRequest
    ) {
        // Step 152 Logic: Emit contract for rendering.
        val contract = request.contract
        _renderingStream.tryEmit(contract)

        // Step 198 Logic: Handover to screen lifecycle participation.
        val handoverRequest = IntelligenceCommandRenderingLifecycleHandoverRequest(contract)
        lifecycleHandoverBoundary.routeToLifecycle(handoverRequest)
    }
}
