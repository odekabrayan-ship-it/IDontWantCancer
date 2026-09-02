package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureFinalityHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandResultClosureConsumptionAcknowledgementResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityProjectionHandoverRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Default implementation of the consumption finality observation boundary.
 * Maps authoritative closure observations to their terminal finality status.
 */
class DefaultIntelligenceCommandResultClosureConsumptionFinalityObservationBoundary @Inject constructor(
    private val closureObservationBoundary: IntelligenceCommandResultClosureObservationBoundary,
    private val finalityHandoverBridge: IntelligenceCommandClosureFinalityHandoverBoundary,
    private val terminalIntegrityHandoverBridge: IntelligenceCommandFinalityTerminalIntegrityHandoverBoundary,
    private val projectionHandoverBridge: IntelligenceCommandTerminalIntegrityProjectionHandoverBoundary,
    private val loopCompletionBoundary: IntelligenceCommandInteractionLoopCompletionBoundary
) : IntelligenceCommandResultClosureConsumptionFinalityObservationBoundary {

    override val finalityStream: Flow<IntelligenceCommandConsumptionFinalityResult> = 
        closureObservationBoundary.closureStream.map { closure ->
            // Step 133 / 148 / 150 / 163 / 164 / 165 / 178 / 179 / 180 / 193 / 194 / 195: Read-only mapping of closure to finality.
            val handoverRequest = IntelligenceCommandClosureFinalityHandoverRequest(closure)
            val finality = finalityHandoverBridge.routeToFinality(handoverRequest)
            
            // Step 164 / 179 / 194: Protect terminal integrity
            val protectedFinality = terminalIntegrityHandoverBridge.routeToIntegrity(finality)

            // Step 165 / 180 / 195: Route to projection
            val projectionHandover = IntelligenceCommandTerminalIntegrityProjectionHandoverRequest(protectedFinality)
            projectionHandoverBridge.routeToProjection(projectionHandover)

            // Step 150: Complete the interaction loop observation
            loopCompletionBoundary.completeLoop(protectedFinality)
            
            protectedFinality
        }
}
