package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandExecutionOutcome

/**
 * Authoritative boundary for converting raw execution outcomes into 
 * authoritative command results.
 */
interface IntelligenceCommandResultClosureConsumptionFinalityExecutionResultBoundary {
    /**
     * Converts an execution outcome into an authoritative command result.
     *
     * @param outcome The raw outcome from the execution authority.
     * @return The authoritative command result (Step 112 product).
     */
    fun establishResult(
        outcome: IntelligenceCommandExecutionOutcome
    ): IntelligenceApplicationCommandResult
}
