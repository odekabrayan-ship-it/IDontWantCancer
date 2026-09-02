package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityPresentationContract
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRenderingLifecycleHandoverRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the screen lifecycle participation boundary.
 * Governs how rendering contracts reach the active UI.
 */
@Singleton
class DefaultIntelligenceCommandRenderingLifecycleBoundary @Inject constructor() : 
    IntelligenceCommandRenderingLifecycleBoundary {

    private val _lifecycleRenderingStream = MutableSharedFlow<CommandConsumptionFinalityPresentationContract>(replay = 1)
    override val lifecycleRenderingStream: Flow<CommandConsumptionFinalityPresentationContract> = 
        _lifecycleRenderingStream.asSharedFlow()

    override fun participate(
        request: IntelligenceCommandRenderingLifecycleHandoverRequest
    ) {
        // Step 198 Logic: Ensure the rendering contract is available for lifecycle-aware collection.
        // Participation is formalized by emitting to the governed stream.
        _lifecycleRenderingStream.tryEmit(request.contract)
    }
}
