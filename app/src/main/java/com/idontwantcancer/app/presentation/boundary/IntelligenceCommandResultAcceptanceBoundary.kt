package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandAcceptanceResult

/**
 * Authoritative boundary through which a downstream authority determines 
 * whether an already-acknowledged command result satisfies the receiving contract.
 */
interface IntelligenceCommandResultAcceptanceBoundary {
    /**
     * Evaluates whether a command result is accepted by the destination authority.
     *
     * @param result The finalized result that was handed off and acknowledged.
     * @return The structured acceptance result.
     */
    suspend fun evaluateAcceptance(
        result: IntelligenceApplicationCommandResult
    ): IntelligenceCommandAcceptanceResult
}
