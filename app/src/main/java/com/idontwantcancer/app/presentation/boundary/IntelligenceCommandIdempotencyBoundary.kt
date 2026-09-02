package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandIdempotencyResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for ensuring that repeated delivery of the same 
 * command follows existing idempotency and identity semantics.
 */
interface IntelligenceCommandIdempotencyBoundary {
    /**
     * Evaluates whether an interaction should be processed based on its 
     * identity and existing system state.
     *
     * @param interaction The UI interaction to evaluate.
     * @param commandIdentity The authoritative identity string (e.g. reentryIdentity or operationId).
     * @return The structured idempotency result.
     */
    fun evaluateIdempotency(
        interaction: IntelligenceUiInteraction,
        commandIdentity: String
    ): IntelligenceCommandIdempotencyResult
}
