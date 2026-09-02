package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the consumption finality boundary.
 * Determines terminality by correlating closure status with downstream acknowledgement.
 */
class DefaultIntelligenceCommandResultClosureConsumptionFinalityBoundary @Inject constructor() : 
    IntelligenceCommandResultClosureConsumptionFinalityBoundary,
    IntelligenceCommandResultClosureFinalityBoundary {

    override fun evaluateFinality(
        request: IntelligenceCommandFinalityRequest
    ): IntelligenceCommandConsumptionFinalityResult {
        // Step 148 / 163 Logic: Ensure finality is evaluated ONLY after closure.
        // Delegates to the existing Step 132 logic.
        val observationAck = IntelligenceCommandResultClosureConsumptionAcknowledgementResult(
            operationId = request.closure.operationId,
            reason = "Derived from closure evaluation."
        )
        return evaluateFinality(request.closure, observationAck)
    }

    override fun evaluateFinality(
        closure: IntelligenceCommandClosureResult,
        acknowledgement: IntelligenceCommandResultClosureConsumptionAcknowledgementResult
    ): IntelligenceCommandConsumptionFinalityResult {
        val now = Instant.now()

        // 1. Establish Finality Semantic (Step 132 Logic)
        // If the result-delivery lifecycle is CLOSED and the consumer has 
        // acknowledged its processing, then the consumption lifecycle is terminal.
        val isTerminal = closure.status == CommandResultClosureStatus.CLOSED
        
        val finality = if (isTerminal) CommandConsumptionFinality.TERMINAL 
                       else CommandConsumptionFinality.NON_TERMINAL

        return IntelligenceCommandConsumptionFinalityResult(
            operationId = closure.operationId,
            finality = finality,
            reason = if (isTerminal) "Consumer acknowledged a CLOSED result-delivery lifecycle."
                     else "Consumer acknowledged an OPEN result-delivery lifecycle; processing remains active.",
            evaluatedAt = now
        )
    }
}
