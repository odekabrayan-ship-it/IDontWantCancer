package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionResultHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionResultRequest
import javax.inject.Inject

/**
 * Default implementation of the execution-to-result bridge.
 * Formalizes the return path and routes it to the authoritative gate (Step 157).
 */
class DefaultIntelligenceCommandExecutionResultBridgeBoundary @Inject constructor(
    private val resultAuthority: IntelligenceCommandExecutionOutcomeBoundary
) : IntelligenceCommandExecutionResultBridgeBoundary {

    override fun routeToResult(
        request: IntelligenceCommandExecutionResultHandoverRequest
    ): IntelligenceApplicationCommandResult {
        // Step 172 / 187 Logic: Formalize the result request
        val resultRequest = IntelligenceCommandExecutionResultRequest(
            interaction = request.interaction,
            outcome = request.outcome
        )
        
        // Route to the authoritative gate (Step 157)
        return resultAuthority.reportOutcome(resultRequest.interaction, resultRequest.outcome)
    }
}
