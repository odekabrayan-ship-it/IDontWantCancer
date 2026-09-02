package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandRecoveryResult

/**
 * Authoritative boundary for determining and coordinating recovery actions 
 * after unsuccessful command outcomes.
 */
interface IntelligenceCommandRecoveryBoundary {
    /**
     * Evaluates a command result to determine if recovery is necessary and permitted.
     *
     * @param result The command result to evaluate (Step 112).
     * @return The structured recovery result.
     */
    suspend fun evaluateRecovery(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandRecoveryResult
}
