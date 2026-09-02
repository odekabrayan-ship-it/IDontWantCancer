package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import javax.inject.Inject

/**
 * Default implementation of the outcome consumption boundary.
 * Authorizes reconciliation outcomes for downstream orchestration.
 */
class DefaultIntelligenceReentryOutcomeConsumptionBoundary @Inject constructor() : 
    IntelligenceReentryOutcomeConsumptionBoundary {

    override fun authorizeOutcome(
        outcome: IntelligenceReentryReconciliationOutcome
    ): IntelligenceReentryAuthorizedOutcome {
        // Deterministic authorization of the outcome.
        // It does not perform transitions or mutations; it only wraps 
        // the outcome in an orchestration-ready authorized contract.
        return IntelligenceReentryAuthorizedOutcome(
            outcome = outcome,
            isAuthorizedForOrchestration = true,
            authorizedAt = Instant.now()
        )
    }
}
