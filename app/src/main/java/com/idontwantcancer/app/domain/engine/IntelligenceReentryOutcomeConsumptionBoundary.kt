package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.IntelligenceReentryAuthorizedOutcome
import com.idontwantcancer.app.domain.model.IntelligenceReentryReconciliationOutcome

/**
 * Narrow interface that defines how authorized orchestration components may 
 * consume a re-entry reconciliation outcome.
 * Ensures that outcomes are handled without granting uncontrolled mutation authority.
 */
interface IntelligenceReentryOutcomeConsumptionBoundary {
    /**
     * Authorizes a specific reconciliation outcome for orchestration.
     *
     * @param outcome The formal outcome from Step 93.
     * @return The authorized outcome consumption contract.
     */
    fun authorizeOutcome(
        outcome: IntelligenceReentryReconciliationOutcome
    ): IntelligenceReentryAuthorizedOutcome
}
