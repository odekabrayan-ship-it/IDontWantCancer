package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandDispositionResult

/**
 * Authoritative boundary through which the receiving side represents the final 
 * disposition of an already-handed-off command result.
 */
interface IntelligenceCommandResultDispositionBoundary {
    /**
     * Calculates the final disposition of a command result.
     *
     * @param result The finalized result to evaluate.
     * @return The structured disposition result.
     */
    suspend fun evaluateDisposition(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandDispositionResult
}
