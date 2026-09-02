package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandClosureResult

/**
 * Authoritative boundary through which the receiving-side result-delivery 
 * lifecycle determines whether its own processing has reached a terminal state.
 */
interface IntelligenceCommandResultClosureBoundary {
    /**
     * Evaluates whether the delivery lifecycle for a command result is closed.
     *
     * @param result The finalized command result being delivered.
     * @return The structured closure result.
     */
    suspend fun evaluateClosure(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandClosureResult
}
