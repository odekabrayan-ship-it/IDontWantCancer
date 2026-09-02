package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.presentation.model.IntelligenceCommandReconciliationResult
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction

/**
 * Authoritative boundary for determining the true state of a command when 
 * its outcome is uncertain or ambiguous.
 */
interface IntelligenceCommandReconciliationBoundary {
    /**
     * Reconciles a command interaction with the authoritative source of truth.
     *
     * @param interaction The UI interaction that produced an uncertain outcome.
     * @param commandIdentity The unique identity of the command.
     * @return The structured reconciliation result.
     */
    suspend fun reconcile(
        interaction: IntelligenceUiInteraction,
        commandIdentity: String
    ): IntelligenceCommandReconciliationResult
}
