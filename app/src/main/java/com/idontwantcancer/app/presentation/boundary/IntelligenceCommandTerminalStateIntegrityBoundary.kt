package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceApplicationCommandResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandConsumptionFinalityResult
import com.idontwantcancer.app.presentation.model.IntelligenceCommandTerminalIntegrityRequest
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that once a command reaches its terminal 
 * finality state, its lifecycle cannot be reopened or mutated.
 */
interface IntelligenceCommandTerminalStateIntegrityBoundary {
    /**
     * Verifies whether a proposed operation or state transition violates 
     * the integrity of a terminal state.
     *
     * @param operationId The unique identifier of the operation.
     * @return True if the operation is permitted (not terminal), false otherwise.
     */
    fun verifyIntegrity(operationId: String): Boolean

    /**
     * Ensures that a result being finalized does not attempt to mutate 
     * an already-finalized terminal state.
     *
     * @param identity The authoritative identity of the command.
     * @param result The result proposed for finalization.
     * @return The verified result (either the new one or the existing terminal one).
     */
    fun protectTerminality(
        identity: String,
        result: IntelligenceApplicationCommandResult
    ): IntelligenceApplicationCommandResult

    /**
     * Ensures that a finality result being established does not attempt to mutate 
     * an already-protected terminal state.
     *
     * @param request The formalized terminal-integrity request from Step 164.
     * @return The verified finality result.
     */
    fun protectFinality(
        request: IntelligenceCommandTerminalIntegrityRequest
    ): IntelligenceCommandConsumptionFinalityResult
}
