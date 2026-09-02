package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult

/**
 * Authoritative boundary through which finalized command results are handed 
 * to the correct existing downstream authority.
 */
interface IntelligenceCommandResultHandoffBoundary {
    /**
     * Performs a deterministic handoff of a command result to its destination.
     *
     * @param result The finalized command result to transfer.
     */
    suspend fun performHandoff(result: IntelligenceApplicationCommandResult)
}
