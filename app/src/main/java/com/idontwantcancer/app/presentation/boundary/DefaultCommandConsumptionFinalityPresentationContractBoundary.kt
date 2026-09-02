package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.mapper.toContract
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandPresentationRenderingHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandProjectionPresentationHandoverRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the presentation contract boundary.
 * Translates read-only projections into UI-safe contracts (Step 151).
 */
@Singleton
class DefaultCommandConsumptionFinalityPresentationContractBoundary @Inject constructor(
    private val renderingHandoverBridge: IntelligenceCommandPresentationRenderingHandoverBoundary
) : CommandConsumptionFinalityPresentationContractBoundary {

    private val _presentationStream = MutableSharedFlow<CommandConsumptionFinalityPresentationContract>(replay = 1)
    override val presentationStream: Flow<CommandConsumptionFinalityPresentationContract> = 
        _presentationStream.asSharedFlow()

    override fun presentProjection(
        request: IntelligenceCommandProjectionPresentationHandoverRequest
    ) {
        // Step 151 / 166 / 181 / 196 / 197 Logic: Convert projection into the presentation contract.
        val contract = request.projection.toContract()
        _presentationStream.tryEmit(contract)

        // Step 167 / 182 / 197: Route contract to rendering
        val handoverRequest = IntelligenceCommandPresentationRenderingHandoverRequest(contract)
        renderingHandoverBridge.routeToRendering(handoverRequest)
    }
}
