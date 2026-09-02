package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionOutcome
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionResultHandoverRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the execution-to-result bridge.
 * Formalizes the result into a request and routes it to the authoritative gate (Step 172).
 */
class DefaultIntelligenceCommandExecutionResultHandoverBoundary @Inject constructor(
    private val resultAuthority: IntelligenceCommandExecutionResultBridgeBoundary
) : IntelligenceCommandExecutionResultHandoverBoundary {

    override fun routeToResult(
        interaction: IntelligenceUiInteraction,
        outcome: IntelligenceCommandExecutionOutcome
    ): IntelligenceApplicationCommandResult {
        // Step 187 Logic: Formalize the result request from the execution outcome.
        val request = IntelligenceCommandExecutionResultHandoverRequest(interaction, outcome)
        
        // Route to the authoritative gate (Step 172)
        return resultAuthority.routeToResult(request)
    }
}
