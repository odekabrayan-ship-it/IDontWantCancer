package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionOutcome
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import javax.inject.Inject

/**
 * Default implementation of the execution outcome bridge.
 * Formalizes the return path from performers back into the result authority.
 */
class DefaultIntelligenceCommandExecutionOutcomeBoundary @Inject constructor(
    private val resultBridge: IntelligenceCommandResultClosureConsumptionFinalityExecutionResultBoundary,
    private val finalizationBoundary: IntelligenceCommandFinalizationBoundary
) : IntelligenceCommandExecutionOutcomeBoundary {

    override fun reportOutcome(
        interaction: IntelligenceUiInteraction,
        outcome: IntelligenceCommandExecutionOutcome
    ): IntelligenceApplicationCommandResult {
        // Step 157 Logic: Convert outcome and finalize result.
        // It prevents the executor from independently declaring completion/finality.
        val result = resultBridge.establishResult(outcome)
        return finalizationBoundary.finalizeResult(interaction, result)
    }
}
