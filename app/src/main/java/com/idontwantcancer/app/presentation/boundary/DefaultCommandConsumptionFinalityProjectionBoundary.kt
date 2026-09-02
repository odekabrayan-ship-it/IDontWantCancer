package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.mapper.toUiState
import com.idontwantcancer.app.presentation.model.CommandConsumptionFinalityUiState
import com.idontwantcancer.app.presentation.model.IntelligenceCommandProjectionPresentationHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of the consumption finality projection boundary.
 * Maps authoritative validated terminal states to read-only presentation models.
 */
@Singleton
class DefaultCommandConsumptionFinalityProjectionBoundary @Inject constructor(
    private val presentationHandoverBridge: IntelligenceCommandProjectionPresentationHandoverBoundary
) : CommandConsumptionFinalityProjectionBoundary {

    private val _finalityProjectionStream = MutableSharedFlow<CommandConsumptionFinalityUiState>(replay = 1)
    override val finalityProjectionStream: Flow<CommandConsumptionFinalityUiState> = 
        _finalityProjectionStream.asSharedFlow()

    override fun projectTerminality(
        request: IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
    ) {
        // Step 150 / 165 / 180 / 195 Logic: Convert authoritative terminal state into the projection model.
        val uiState = request.finalityResult.toUiState()
        _finalityProjectionStream.tryEmit(uiState)

        // Step 166 / 181 / 196: Route projection to presentation
        val request = IntelligenceCommandProjectionPresentationHandoverRequest(uiState)
        presentationHandoverBridge.routeToPresentation(request)
    }
}
